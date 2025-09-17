package co.com.crediya.model.solicitud.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public enum StatusName {

    INITITED(UUID.fromString("90c6d49c-cae5-464f-8594-17ca3510e79b")),
    MANUAL_REVIEW(UUID.fromString("caae6c2c-14a6-4607-ba43-23d230ee901a")),
    PENDING_TO_CHECK(UUID.fromString("4f646f64-e460-43d1-9137-0201d4eb3743")),
    REJECTED(UUID.fromString("dd8e57c4-598d-4c34-abe0-618cfe8c48e6")),
    CANCEL_FOR_USER(UUID.fromString("28687783-cf20-40ef-8b63-37a855f2b930")),
    APPROVED(UUID.fromString("25f823ed-fd38-46c0-824d-19382ff914fd"));

    private final UUID id;

    StatusName(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    private static final Map<UUID, StatusName> BY_ID =
            Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(StatusName::getId, e -> e));

    private static final Map<String, StatusName> BY_NAME =
            Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(Enum::name, e -> e));

    public static Optional<StatusName> fromId(UUID id) {
        return Optional.ofNullable(BY_ID.get(id));
    }

    public static Optional<StatusName> fromName(String name) {
        return Optional.ofNullable(BY_NAME.get(name));
    }

    public static Optional<String> getNameById(UUID id) {
        return fromId(id).map(StatusName::name);
    }
    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
