package lescarpi.me.notes.note.dto;

import tools.jackson.databind.JsonNode;

public record NoteRequest(
        JsonNode content
) {
}
