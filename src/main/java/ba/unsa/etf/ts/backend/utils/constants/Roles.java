package ba.unsa.etf.ts.backend.utils.constants;

public enum Roles {
    USER("USER"),
    ADMIN("ADMIN");

    private final String roleName;

    Roles(final String roleName){
        this.roleName = roleName;
    }
    @Override
    public String toString() {
        return roleName;
    }
}
