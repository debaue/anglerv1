package game;


import data.BaitRegistry;
import data.ShopItem;
import entities.Bait;
import entities.Player;
import entities.Rod;
import util.InputHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

    public class ShopSystem {
        private final List<ShopItem> items = new ArrayList<>();
        private int selectedIndex = 0;

        private Rectangle sellAllButton = new Rectangle();
        private Rectangle buyButton = new Rectangle();
        private final List<Rectangle> itemRects = new ArrayList<>();

        public ShopSystem() {
            loadDefaultItems();
        }

        private void loadDefaultItems() {
            for (Rod rod : data.RodRegistry.ALL) {
                items.add(new ShopItem(
                        "rod_" + rod.name.toLowerCase().replace(" ", "_"),
                        rod.name,
                        rod.price,
                        ShopItem.Type.ROD,
                        rod
                ));
            }
            for (Bait bait : BaitRegistry.ALL) {
                if (bait.price == 0) continue;
                items.add(new ShopItem(
                        "bait_" + bait.name.toLowerCase().replace(" ", "_").replace("-", "_"),
                        bait.name,
                        bait.price,
                        ShopItem.Type.BAIT,
                        bait
                ));
            }
        }

        public void update(Player player, InputHandler input) {
            if (input.up) {
                selectedIndex = Math.max(0, selectedIndex - 1);
                input.up = false;
            }
            if (input.down) {
                selectedIndex = Math.min(items.size() - 1, selectedIndex + 1);
                input.down = false;
            }

            if (input.spacePressed) {
                buySelected(player);
                input.spacePressed = false;
            }

            if (input.mouseClicked) {
                handleMouseClick(player, input.mouseX, input.mouseY);
                input.mouseClicked = false;
            }
        }

        public void handleMouseClick(Player player, int mouseX, int mouseY) {
            if (sellAllButton.contains(mouseX, mouseY)) {
                player.sellAllFish();
                return;
            }

            if (buyButton.contains(mouseX, mouseY)) {
                buySelected(player);
                return;
            }

            for (int i = 0; i < itemRects.size(); i++) {
                if (itemRects.get(i).contains(mouseX, mouseY)) {
                    selectedIndex = i;
                    return;
                }
            }
        }

        public boolean buySelected(Player player) {
            if (selectedIndex < 0 || selectedIndex >= items.size()) return false;

            ShopItem item = items.get(selectedIndex);
            if (player.getGold() < item.getPrice()) return false;

            if (item.getType() == ShopItem.Type.ROD) {
                Rod rod = (Rod) item.getPayload();
                if (player.getEquippedRod() != null &&
                        rod.tier <= player.getEquippedRod().tier) {
                    return false;
                }
                player.addGold(-item.getPrice());
                player.setEquippedRod(rod);
                return true;
            }

            if (item.getType() == ShopItem.Type.BAIT) {
                Bait bait = (Bait) item.getPayload();
                player.addGold(-item.getPrice());
                player.addBait(bait, 5);
                return true;
            }

            return false;
        }

        public List<ShopItem> getItems() { return items; }
        public int getSelectedIndex() { return selectedIndex; }
        public void setSelectedIndex(int selectedIndex) { this.selectedIndex = selectedIndex; }

        public Rectangle getSellAllButton() { return sellAllButton; }
        public Rectangle getBuyButton() { return buyButton; }
        public List<Rectangle> getItemRects() { return itemRects; }

        public void setSellAllButton(Rectangle r) { sellAllButton = r; }
        public void setBuyButton(Rectangle r) { buyButton = r; }

        public void rebuildItemRects(int x, int y, int width, int rowHeight) {
            itemRects.clear();
            for (int i = 0; i < items.size(); i++) {
                itemRects.add(new Rectangle(x, y + i * rowHeight, width, rowHeight - 6));
            }
        }
    }
