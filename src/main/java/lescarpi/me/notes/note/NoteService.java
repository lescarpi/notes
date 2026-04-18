package lescarpi.me.notes.note;

import lescarpi.me.notes.note.dto.NoteRequest;
import lescarpi.me.notes.note.dto.NoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class NoteService{

    private final NoteRepository repository;

    public NoteResponse create(NoteRequest request) {
        Note note = new Note(
                UUID.randomUUID(),
                request.content(),
                OffsetDateTime.now()
        );

        repository.save(note);

        return new NoteResponse(note);
    }

    public NoteResponse findById(UUID id) {
        Note note = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note não encontrada"));

        return new NoteResponse(note);
    }

    public List<NoteResponse> findAll(int page, int size) {
        int offset = page * size;

        return repository.findAll(size, offset)
                .stream()
                .map(NoteResponse::new)
                .toList();
    }

    public NoteResponse update(UUID id, NoteRequest dto) {
        Note existingNote = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note não encontrada"));

        Note updatedNote = new Note(
                existingNote.id(),
                dto.content(),
                existingNote.createdAt()
        );

        repository.update(updatedNote);

        return new NoteResponse(updatedNote);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Note não encontrada");
        }

        repository.deleteById(id);
    }

}