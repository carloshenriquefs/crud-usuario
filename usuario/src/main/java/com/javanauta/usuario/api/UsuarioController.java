package com.javanauta.usuario.api;

import com.javanauta.usuario.api.request.UsuarioRequestDTO;
import com.javanauta.usuario.api.response.UsuarioResponseDTO;
import com.javanauta.usuario.business.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping()
    public ResponseEntity<UsuarioResponseDTO> gravarDadosUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return ResponseEntity.ok(usuarioService.gravarUsuarios(usuarioRequestDTO));
    }

    @GetMapping()
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarDadosUsuario(email));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarDadosUsuario(@RequestParam("email") String email) {
        usuarioService.deletarDadosUsuario(email);
        return ResponseEntity.accepted().build();
    }
}
