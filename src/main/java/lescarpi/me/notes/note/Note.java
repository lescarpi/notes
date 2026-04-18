package lescarpi.me.notes.note;

import tools.jackson.databind.JsonNode;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Note(
        UUID id,
        JsonNode content,
        OffsetDateTime createdAt
) {
}
