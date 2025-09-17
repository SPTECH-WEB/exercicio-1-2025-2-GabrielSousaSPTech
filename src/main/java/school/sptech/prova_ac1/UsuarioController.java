package school.sptech.prova_ac1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

   private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {

        try{
            return ResponseEntity.ok().body(repository.findAll());
        }catch(Error e){
            return ResponseEntity.internalServerError().build();
        }


    }
    @PostMapping
    public ResponseEntity<Usuario> criar( @RequestBody Usuario usuario) {
        try{
            return ResponseEntity.status(201).body(repository.save(usuario));
        }catch(Error e){
            return ResponseEntity.internalServerError().build();
        }

    }
    @GetMapping("{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        try{
            return repository.findById(id)
                    .map(usuario -> ResponseEntity.ok().body(usuario))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }catch(Error e) {
            return ResponseEntity.internalServerError().build();
        }
    }
@DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable  Integer id) {
        try{
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch(Error e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/filtro-data?nascimento={nascimento}")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(LocalDate nascimento) {
        try{
            return ResponseEntity.ok().body(repository.findByDataNascimeto(nascimento));

        }catch(Error e) {
            return ResponseEntity.internalServerError().build();
        }
    }
@PutMapping("{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Integer id,
            @RequestBody Usuario usuario
    ) {
        try{
            return repository.findById(id).map(usuarioAtual->{
                if(usuario.getNome() !=null){
                    usuarioAtual.setNome(usuario.getNome());
                }
                if(usuario.getCpf() !=null){
                    usuarioAtual.setCpf(usuario.getCpf());
                }
                if(usuario.getDataNascimento() !=null){
                    usuarioAtual.setDataNascimento(usuario.getDataNascimento());
                }
                if(usuario.getSenha() !=null){
                    usuarioAtual.setSenha(usuario.getSenha());
                }

                return repository.save(usuario);
            }).orElseGet(() -> ResponseEntity.notFound().build());
        }catch(Error e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
