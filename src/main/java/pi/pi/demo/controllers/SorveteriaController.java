package pi.pi.demo.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pi.pi.demo.models.Cliente;
import pi.pi.demo.models.Sorvete;
import pi.pi.demo.repositories.ClienteRepository;
import pi.pi.demo.repositories.SorveteRepository;

@Controller
@RequestMapping("/sorveteria")

public class SorveteriaController {

	@Autowired
	private SorveteRepository sr;
	@Autowired
	private ClienteRepository cr;


	@RequestMapping("/form")
	public String form(Sorvete sorvete) {
		return "paginas/form";
	}

	@PostMapping("/add")
	private String save(@Valid Sorvete dados, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return form(dados);
		}
		System.out.println(dados);
		sr.save(dados);
		attributes.addFlashAttribute("mensagem", "Sorvete salvo com sucesso!");
		return "redirect:/sorveteria";
	}

	@GetMapping
	private ModelAndView listarSorvetes() {
		List<Sorvete> sorvetes = sr.findAll();
		ModelAndView mv = new ModelAndView("paginas/lista");
		mv.addObject("sorvetes", sorvetes);
		return mv;
	}

	@GetMapping("/{id}")
	public ModelAndView detalharDados(@PathVariable long id, Cliente cliente) {
		ModelAndView md = new ModelAndView();
		Optional<Sorvete> opt = sr.findById(id);
		if (opt.isEmpty()) {
			md.setViewName("redirect:/sorveteria");
			return md;
		}
		md.setViewName("paginas/detalhes");
		Sorvete sorvete = opt.get();
		md.addObject("sorvete", sorvete);
		List<Cliente> clientes = cr.findBySorvete(sorvete);
		md.addObject("clientes", clientes);

		return md;
	}

	@PostMapping("/{idSorvete}")
	public ModelAndView salvarCliente(@PathVariable Long idSorvete, @Valid Cliente cliente, BindingResult result,
			RedirectAttributes attributes) {
		ModelAndView md = new ModelAndView();
		if (result.hasErrors()) {
			return detalharDados(idSorvete, cliente);
		}
		Optional<Sorvete> opt = sr.findById(idSorvete);

		if (opt.isEmpty()) {
			md.setViewName("redirect:/sorveteria");
			return md;
		}

		Sorvete sorvete = opt.get();
		cliente.setSorvete(sorvete);

		cr.save(cliente);
		attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
		md.setViewName("redirect:/sorveteria/{idSorvete}");
		return md;
	}

	@GetMapping("/{id}/selecionar")
	public ModelAndView selecionarSorvete(@PathVariable Long id) {
		ModelAndView md = new ModelAndView();
		Optional<Sorvete> opt = sr.findById(id);

		if (opt.isEmpty()) {
			md.setViewName("redirect:/sorveteria");
			return md;
		}
		Sorvete sorvete = opt.get();
		md.setViewName("paginas/form");
		md.addObject("sorvete", sorvete);
		return md;
	}

	@GetMapping("/{idSorvete}/clientes/{idCliente}/selecionar")
	public ModelAndView selecionarCliente(@PathVariable Long idSorvete, @PathVariable Long idCliente) {
		ModelAndView md = new ModelAndView();
		Optional<Cliente> optCliente = cr.findById(idCliente);
		Optional<Sorvete> optSorvete = sr.findById(idSorvete);
		if (optCliente.isEmpty() || optSorvete.isEmpty()) {
			md.setViewName("redirect:/sorveteria");
			return md;
		}
		Sorvete sorvete = optSorvete.get();
		Cliente cliente = optCliente.get();

		if (sorvete.getId() != cliente.getSorvete().getId()) {
			md.setViewName("redirect:/sorveteria");
			return md;
		}
		md.setViewName("paginas/detalhes");
		md.addObject("cliente", cliente);
		md.addObject("sorvete", sorvete);
		md.addObject("clientes", cr.findBySorvete(sorvete));

		return md;
	}

	@GetMapping("/{id}/remover")
	public String deletarSorvete(@PathVariable Long id, RedirectAttributes attributes) {
		Optional<Sorvete> opt = sr.findById(id);

		if (!opt.isEmpty()) {
			Sorvete sorvete = opt.get();

			List<Cliente> clientes = cr.findBySorvete(sorvete);
			cr.deleteAll(clientes);
			sr.delete(sorvete);
			attributes.addFlashAttribute("mensagem", "Sorvete deletado com sucesso!");
		}
		return "redirect:/sorveteria";
	}

	@GetMapping("/{idSorvete}/clientes/{idCliente}/deletar")
	public ModelAndView deletarCliente(@PathVariable Long idSorvete, @PathVariable Long idCliente,
			RedirectAttributes attributes) {
		ModelAndView md = new ModelAndView();
		Optional<Sorvete> optSorvete = sr.findById(idSorvete);
		Optional<Cliente> optCliente = cr.findById(idCliente);

		if (!optCliente.isEmpty()) {
			Cliente cliente = optCliente.get();

			cr.delete(cliente);
			attributes.addFlashAttribute("mensagem", "Cliente deletado com sucesso!");
		}
		Sorvete sorvete = optSorvete.get();
		md.setViewName("paginas/detalhes");
		md.addObject("sorvete", sorvete);
		md.addObject("cliente", new Cliente());
		md.addObject("clientes", cr.findBySorvete(sorvete));
		return md;

	}

}
