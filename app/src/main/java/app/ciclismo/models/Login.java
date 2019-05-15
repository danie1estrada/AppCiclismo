package app.ciclismo.models;

public class Login {

    private String id;
    private int ttl;
    private String created;
    private String userId;

    public Login(String id, int ttl, String created, String userId) {
        this.id = id;
        this.ttl = ttl;
        this.created = created;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public int getTtl() {
        return ttl;
    }

    public String getCreated() {
        return created;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return String.format("id (token): %s\nttl: %d\nuserId: %s", id, ttl, userId);
    }
}
