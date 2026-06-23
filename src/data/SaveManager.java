package data;

import entities.*;
import org.json.JSONArray;
import org.json.JSONObject;
import world.FishingZone;
import world.ZoneRegistry;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class SaveManager {

    private static final String BASE = "https://facescore.de/api";


    public static void saveGame(Player player) {
        new Thread(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("name",     player.getName());
                json.put("gold",     player.getGold());
                json.put("rod",      player.getEquippedRod() != null ? player.getEquippedRod().name : "");
                json.put("bait",     player.getEquippedBait() != null ? player.getEquippedBait().name : "");
                json.put("baitCount", player.getBaitCount());
                json.put("maxSlots", player.getMaxSlots());

                JSONArray zones = new JSONArray();
                for (FishingZone z : ZoneRegistry.ALL) {
                    if (player.isZoneUnlocked(z)) zones.put(z.name);
                }
                json.put("unlockedZones", zones);

                JSONArray inv = new JSONArray();
                for (Fish f : player.getInventory()) {
                    JSONObject fo = new JSONObject();
                    fo.put("type",   f.type.name);
                    fo.put("weight", f.weightKg);
                    fo.put("length", f.lengthCm);
                    fo.put("time",   f.caughtAt.toString());
                    inv.put(fo);
                }
                json.put("inventory", inv);

                JSONArray book = new JSONArray();
                for (FishBookEntry e : player.getFishBook().values()) {
                    JSONObject eo = new JSONObject();
                    eo.put("fish",     e.getFishName());
                    eo.put("count",    e.getCaughtCount());
                    eo.put("heaviest", e.getHeaviestWeight());
                    eo.put("longest",  e.getLongestLength());
                    book.put(eo);
                }
                json.put("fishBook", book);

                post(BASE + "/save", json.toString());
            } catch (Exception e) {
            }
        }, "save-thread").start();
    }

    public static JSONObject loadGame(String name) {
        try {
            String encoded = name.replaceAll("[^a-zA-Z0-9_-]", "_");
            String response = get(BASE + "/load/" + encoded);
            if (response == null || response.startsWith("{\"error\"")) return null;
            return new JSONObject(response);
        } catch (Exception e) {
            return null;
        }
    }

    public static void checkSaveExists(String name, java.util.function.Consumer<Boolean> onSaveExists, java.util.function.Consumer<Boolean> onReachable) {
        new Thread(() -> {
            try {
                String encoded = name.replaceAll("[^a-zA-Z0-9_-]", "_");
                String response = get(BASE + "/load/" + encoded);
                boolean exists = response != null && !response.startsWith("{\"error\"");
                onReachable.accept(true);
                onSaveExists.accept(exists);
            } catch (Exception e) {
                onReachable.accept(false);
                onSaveExists.accept(false);
            }
        }, "check-save-thread").start();
    }

    public static void applyToPlayer(JSONObject data, Player player) {
        player.setName(data.optString("name", player.getName()));
        int gold = data.optInt("gold", 0);
        player.addGold(gold - player.getGold());
        player.setMaxSlots(data.optInt("maxSlots", 10));

        String rodName = data.optString("rod", "");
        if (!rodName.isEmpty()) {
            for (Rod rod : RodRegistry.ALL) {
                if (rod.name.equals(rodName)) { player.setEquippedRod(rod); break; }
            }
        }

        String baitName = data.optString("bait", "");
        int baitCount   = data.optInt("baitCount", -1);
        if (!baitName.isEmpty()) {
            for (Bait bait : BaitRegistry.ALL) {
                if (bait.name.equals(baitName)) { player.stackBait(bait, baitCount); break; }
            }
        }

        JSONArray zones = data.optJSONArray("unlockedZones");
        if (zones != null) {
            for (int i = 0; i < zones.length(); i++) {
                String zn = zones.getString(i);
                for (FishingZone z : ZoneRegistry.ALL) {
                    if (z.name.equals(zn)) player.unlockZone(z);
                }
            }
        }

        JSONArray inv = data.optJSONArray("inventory");
        if (inv != null) {
            player.getInventory().clear();
            for (int i = 0; i < inv.length() && i < player.getMaxSlots(); i++) {
                JSONObject fo = inv.getJSONObject(i);
                FishType type = FishRegistry.findByName(fo.getString("type"));
                if (type != null) {
                    Fish f = new Fish(type, (float) fo.getDouble("weight"),
                            (float) fo.getDouble("length"), LocalDateTime.parse(fo.getString("time")));
                    player.getInventory().add(f);
                }
            }
        }

        JSONArray book = data.optJSONArray("fishBook");
        if (book != null) {
            player.getFishBook().clear();
            for (int i = 0; i < book.length(); i++) {
                JSONObject eo = book.getJSONObject(i);
                FishBookEntry entry = new FishBookEntry(eo.getString("fish"));
                entry.restoreStats(eo.getInt("count"),
                        (float) eo.getDouble("heaviest"),
                        (float) eo.getDouble("longest"));
                player.getFishBook().put(entry.getFishName(), entry);
            }
        }
    }

    private static void post(String url, String body) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        try (OutputStream os = con.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }
        con.getResponseCode();
        con.disconnect();
    }

    private static String get(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        int code = con.getResponseCode();
        InputStream is = code >= 400 ? con.getErrorStream() : con.getInputStream();
        if (is == null) return null;
        String result = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        con.disconnect();
        return result;
    }
}
