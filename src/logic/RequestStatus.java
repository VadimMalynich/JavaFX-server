package logic;

public enum RequestStatus {
    APPROVED("Одобрен"), DENIED("Отказано"), IN_PROCESSING("В обработке");
    private String value;

    RequestStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
