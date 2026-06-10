package data;

public class ShopItem {
    public enum Type {
        ROD,
        BAIT,
        GENERIC
    }

    private final String id;
    private final String name;
    private final int price;
    private final Type type;
    private final Object payload;

    public ShopItem(String id, String name, int price, Type type, Object payload) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.payload = payload;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public Type getType() { return type; }
    public Object getPayload() { return payload; }
}