package fr.norehc.test.enums;

public enum InventoryNameEnums {

    PLAYERBANK("§6Interface de gestion de guilde"),
    GUILDBANK(""),
    GLOBALGUILD("§6Interface de gestion de guilde"),
    ADMIN("§4admin access"),
    ACCESADMIN("§4Acces a l'interface admin")
    ;

    private String title;

    InventoryNameEnums(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
