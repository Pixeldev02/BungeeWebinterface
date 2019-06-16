package de.chrxssxdev.bungeeweb.Enums;

public enum  AccountType {

    ADMIN("Admin"), TEAM("Team");

    public String name;

    AccountType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
