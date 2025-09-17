package co.com.crediya.model.solicitud.valueobjects;

public record SortSpec(String property, Direction direction) {
    public enum Direction { ASC, DESC }

    public static SortSpec fromApi(String apiField, String dir) {
        String property = switch (apiField.toLowerCase()) {
            case "created_at"      -> "createdAt";
            case "updated_at"      -> "updatedAt";
            case "requested_amount"-> "requestedAmount";
            case "loan_term"       -> "loanTerm";
            case "email"           -> "email";
            case "name"            -> "name";
            default                -> "createdAt";
        };

        Direction direction = "DESC".equalsIgnoreCase(dir) ? Direction.DESC : Direction.ASC;
        return new SortSpec(property, direction);
    }
}