package school.sptech.prova_ac1;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.ArrayList;
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
           List<Usuario> usuarios = repository.findAll();
            if(usuarios.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.status(HttpStatus.OK).body(usuarios);
        }catch(Error e){
            return ResponseEntity.internalServerError().build();
        }


    }
    @PostMapping
    public ResponseEntity<Usuario> criar( @RequestBody Usuario usuario) {
        try{
            if((repository.findByCpf(usuario.getCpf()).stream().count() >0)||(repository.findByEmail(usuario.getEmail()).stream().count() >0)){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            };
            return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(usuario));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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


    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(@RequestParam LocalDate nascimento) {
        try{
            try{
                List<Usuario> usuarios = repository.findByDataNascimentoAfter(nascimento);
                if (usuarios.isEmpty()) {
                    return ResponseEntity.noContent().build(); // 204
                }

                return ResponseEntity.status(HttpStatus.OK).body(usuarios);
            }catch(Error e){
                return ResponseEntity.internalServerError().build();
            }

        }catch(Error e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PutMapping("{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Integer id,
            @RequestBody Usuario usuario
    ) {
        try {
            if((repository.findByCpf(usuario.getCpf()).stream().anyMatch(idIgual ->!idIgual.getId().equals(id)))||(repository.findByEmail(usuario.getEmail()).stream().anyMatch(idIgual->!idIgual.getId().equals(id)))) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return repository.findById(id)
                    .map(usuarioAtual -> {
                        if (usuario.getNome() != null) {
                            usuarioAtual.setNome(usuario.getNome());
                        }
                        if (usuario.getEmail() != null) {
                            usuarioAtual.setEmail(usuario.getEmail());
                        }
                        if (usuario.getCpf() != null) {
                            usuarioAtual.setCpf(usuario.getCpf());
                        }
                        if (usuario.getDataNascimento() != null) {
                            usuarioAtual.setDataNascimento(usuario.getDataNascimento());
                        }
                        if (usuario.getSenha() != null) {
                            usuarioAtual.setSenha(usuario.getSenha());
                        }

                        Usuario atualizado = repository.save(usuarioAtual);
                        return ResponseEntity.ok(atualizado);
                    })
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
