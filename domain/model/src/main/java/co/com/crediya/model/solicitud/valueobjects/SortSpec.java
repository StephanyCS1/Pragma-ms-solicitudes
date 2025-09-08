package co.com.crediya.model.solicitud.valueobjects;

public record SortSpec(String property, Direction direction) {
    public enum Direction { ASC, DESC }
}