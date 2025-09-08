package co.com.crediya.model.solicitud.valueobjects;

public record Name(String firstName, String lastName) {

    public String fullName() {
        return capitalize(firstName) + " " + capitalize(lastName);
    }

    private String capitalize(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        text = text.trim().toLowerCase();
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}