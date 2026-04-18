package lescarpi.me.notes.note.dto;

import lescarpi.me.notes.note.Note;
import tools.jackson.databind.JsonNode;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NoteResponse(
        UUID id,
        JsonNode content,
        OffsetDateTime createdAt
) {
        public NoteResponse(Note note) {
            this(note.id(), note.content(), note.createdAt());
        }
}
