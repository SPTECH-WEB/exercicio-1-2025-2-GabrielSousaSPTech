package school.sptech.prova_ac1;

import kotlin.reflect.jvm.internal.impl.load.java.structure.JavaPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByDataNascimentoAfter(LocalDate dataNascimento);
    List<Usuario> findByCpf(String cpf);
    List<Usuario> findByEmail(String email);

}
