# Angler

Ein 2D Top-Down Angelspiel in Java mit Swing.

## Voraussetzungen

- Java 21
- `lib/json.jar` (liegt im Projektordner)

## Starten

In IntelliJ IDEA: `Main.java` → Run


## Spielanleitung

| Taste | Aktion |
|-------|--------|
| WASD | Bewegen |
| F | Angeln (in Wassernähe) |
| E | Shop öffnen (beim Händler) |
| I | Inventar |
| B | Fischbuch |
| ESC | Pause / Menü schließen |
| Maus | Klicken zum Angeln / Kaufen |

## Features

- Angeln mit Minigame (Timing-basiert)
- Shop: Ruten, Köder, Zonen, Inventar-Erweiterungen
- 3 Angelzonen: Startteich, Ostsee, Südsee
- Fischbuch mit Rekorden
- Online Speichern & Laden via `Domain` zum Homeserver

## Projektstruktur

```
src/
  Main.java
  game/         – Spiellogik (Game, FishingSystem, ShopSystem)
  game/renderer/ – Zeichenklassen pro Screen
  entities/     – Spielobjekte (Player, Fish, Rod, Bait, ...)
  data/         – Registrys, SaveManager
  world/        – TileMap, Zonen, Kamera
  util/         – InputHandler, SpriteLoader, MusicPlayer
```
