package br.com.cotiinformatica.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.cotiinformatica.entities.Compromisso;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.repositories.CompromissoRepository;

@Controller
public class HomeController {

	@RequestMapping(value = "/home")
	public ModelAndView home(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("agenda/home");

		try {

			String mes = new SimpleDateFormat("MM").format(new Date());
			int mesVigente = Integer.parseInt(mes);

			Date dataInicio = getFirstDayOfMonth(mesVigente);
			Date dataFim = getLastDayOfMonth(mesVigente);

			modelAndView.addObject("data_inicio", dataInicio);
			modelAndView.addObject("data_fim", dataFim);
			modelAndView.addObject("mes_vigente", mesVigente);

			Usuario usuario = (Usuario) request.getSession().getAttribute("usuario_autenticado");
			CompromissoRepository compromissoRepository = new CompromissoRepository();
			List<Compromisso> lista = compromissoRepository.obterTodos(usuario.getIdUsuario(), dataInicio, dataFim);

			modelAndView.addObject("compromissos", lista);
			
			int qtdPrioridadeBaixa = 0;
			int qtdPrioridadeMedia = 0;
			int qtdPrioridadeAlta = 0;
			
			for(Compromisso item : lista) {
				if(item.getPrioridade() == 1) qtdPrioridadeAlta++;
				if(item.getPrioridade() == 2) qtdPrioridadeMedia++;
				if(item.getPrioridade() == 3) qtdPrioridadeBaixa++;
				
			}

			modelAndView.addObject("qtd_prioridade_baixa", qtdPrioridadeBaixa);
			modelAndView.addObject("qtd_prioridade_media", qtdPrioridadeMedia);
			modelAndView.addObject("qtd_prioridade_alta", qtdPrioridadeAlta);
			
		} catch (Exception e) {

		}

		return modelAndView;
	}

	@RequestMapping(value = "/pesquisar-compromissos-mes", method = RequestMethod.POST)
	public ModelAndView pesquisarCompromissos(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("agenda/home");

		try {
			int mesVigente = Integer.parseInt(request.getParameter("mes"));

			Date dataInicio = getFirstDayOfMonth(mesVigente);
			Date dataFim = getLastDayOfMonth(mesVigente);

			modelAndView.addObject("data_inicio", dataInicio);
			modelAndView.addObject("data_fim", dataFim);
			modelAndView.addObject("mes_vigente", mesVigente);

			Usuario usuario = (Usuario) request.getSession().getAttribute("usuario_autenticado");

			CompromissoRepository compromissoRepository = new CompromissoRepository();
			List<Compromisso> lista = compromissoRepository.obterTodos(usuario.getIdUsuario(), dataInicio, dataFim);

			modelAndView.addObject("compromissos", lista);
			
			int qtdPrioridadeBaixa = 0;
			int qtdPrioridadeMedia = 0;
			int qtdPrioridadeAlta = 0;
			
			for(Compromisso item : lista) {
				if(item.getPrioridade() == 1) qtdPrioridadeAlta++;
				if(item.getPrioridade() == 2) qtdPrioridadeMedia++;
				if(item.getPrioridade() == 3) qtdPrioridadeBaixa++;
				
			}

			modelAndView.addObject("qtd_prioridade_baixa", qtdPrioridadeBaixa);
			modelAndView.addObject("qtd_prioridade_media", qtdPrioridadeMedia);
			modelAndView.addObject("qtd_prioridade_alta", qtdPrioridadeAlta);

		} catch (Exception e) {
			modelAndView.addObject("mensagem_erro", e.getMessage());

		}

		return modelAndView;
	}

	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpServletRequest request) {

		request.getSession().removeAttribute("usuario_autenticado");

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/");

		return modelAndView;
	}

	private Date getFirstDayOfMonth(int mes) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		calendar.set(Calendar.MONTH, mes - 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();

	}

	private Date getLastDayOfMonth(int mes) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		calendar.set(Calendar.MONTH, mes - 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();

	}

}
