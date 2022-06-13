package br.com.cotiinformatica.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.repositories.UsuarioRepository;

@Controller
public class RegisterController {

	@RequestMapping(value = "/register-user")
	public ModelAndView register() {

		ModelAndView modelAndView = new ModelAndView("register");
		return modelAndView;
	}

	@RequestMapping(value = "/cadastrar-usuario", method = RequestMethod.POST)
	public ModelAndView cadastrarUsuario(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("register");

		try {

			String nome = request.getParameter("nome");
			String email = request.getParameter("email");
			String senha = request.getParameter("senha");
			String senhaConfirmacao = request.getParameter("senhaConfirmacao");

			if (!senha.equals(senhaConfirmacao)) {
				throw new Exception("Senhas não conferem, por favor verifique.");
			}

			UsuarioRepository usuarioRepository = new UsuarioRepository();
			if (usuarioRepository.obterPorEmail(email) != null) {
				throw new Exception("O email " + email + " já está cadastrado, por favor tente outro.");
			}

			Usuario usuario = new Usuario();
			usuario.setNome(nome);
			usuario.setEmail(email);
			usuario.setSenha(senha);

			usuarioRepository.inserir(usuario);

			modelAndView.addObject("mensagem_sucesso",
					"Parabéns " + usuario.getNome() + ", sua conta foi criada com sucesso.");
		} catch (Exception e) {
			modelAndView.addObject("mensagem_erro", e.getMessage());
		}

		return modelAndView;
	}

}
