package com.javanauta.usuario.business;

import com.javanauta.usuario.api.converter.UsuarioConverter;
import com.javanauta.usuario.api.converter.UsuarioMapper;
import com.javanauta.usuario.api.request.UsuarioRequestDTO;
import com.javanauta.usuario.api.response.UsuarioResponseDTO;
import com.javanauta.usuario.infrastructure.entity.EnderecoEntity;
import com.javanauta.usuario.infrastructure.entity.UsuarioEntity;
import com.javanauta.usuario.infrastructure.exceptions.BusinessException;
import com.javanauta.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.javanauta.usuario.constants.Constants.DADOS_DO_USUARIO_SAO_OBRIGATORIOS;
import static com.javanauta.usuario.constants.Constants.ERRO_AO_BUSCAR_DADOS_DE_USUARIO;
import static com.javanauta.usuario.constants.Constants.USUARIO_NAO_ENCONTRADO;
import static com.javanauta.usuario.constants.Constants.ERRO_AO_GRAVAR_DADOS_DO_USUARIO;

import static org.springframework.util.Assert.notNull;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final UsuarioMapper usuarioMapper;
    private final EnderecoService enderecoService;

    public UsuarioEntity salvarUsuario(UsuarioEntity usuarioEntity) {
        return usuarioRepository.save(usuarioEntity);
    }

    public UsuarioResponseDTO gravarUsuarios(UsuarioRequestDTO usuarioRequestDTO) {
        try {
            notNull(usuarioRequestDTO, DADOS_DO_USUARIO_SAO_OBRIGATORIOS);

            UsuarioEntity usuarioEntity = salvarUsuario(usuarioConverter.paraUsuarioEntity(usuarioRequestDTO));

            EnderecoEntity enderecoEntity = enderecoService.salvarEndereco(
                    usuarioConverter.paraEnderecoEntity(usuarioRequestDTO.getEndereco(), usuarioEntity.getId()));

            return usuarioMapper.paraUsuarioResponseDTO(usuarioEntity, enderecoEntity);
        } catch (Exception e) {
            throw new BusinessException(ERRO_AO_BUSCAR_DADOS_DE_USUARIO, e);
        }
    }

    public UsuarioResponseDTO buscarDadosUsuario(String email) {
        try {
            UsuarioEntity entity = usuarioRepository.findByEmail(email);

            notNull(entity, USUARIO_NAO_ENCONTRADO);

            EnderecoEntity enderecoEntity = enderecoService.findByUsuarioId(entity.getId());

            return usuarioMapper.paraUsuarioResponseDTO(entity, enderecoEntity);
        } catch (Exception e) {
            throw new BusinessException(ERRO_AO_GRAVAR_DADOS_DO_USUARIO, e);
        }
    }

    @Transactional
    public void deletarDadosUsuario(String email) {
        UsuarioEntity entity = usuarioRepository.findByEmail(email);
        usuarioRepository.deleteByEmail(email);
        enderecoService.deleteByUsuarioId(entity.getId());
    }
}
