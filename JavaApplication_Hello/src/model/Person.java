package model;

public abstract class Person {

    protected String idCard;
    protected String name;

    public Person(String idCard, String name) {
        this.idCard = idCard;
        this.name   = name;
    }

    public String getIdCard() { return idCard; }
    public String getName()   { return name;   }

    public void setIdCard(String idCard) { this.idCard = idCard; }
    public void setName(String name)     { this.name   = name;   }

    @Override
    public abstract String toString();
}