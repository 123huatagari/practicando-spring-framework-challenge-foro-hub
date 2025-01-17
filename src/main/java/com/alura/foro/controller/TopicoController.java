package com.alura.foro.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alura.foro.persistence.DTO.ResponsePerfil;
import com.alura.foro.persistence.DTO.ResponseRespuestas;
import com.alura.foro.persistence.DTO.ResponseTopico;
import com.alura.foro.persistence.entity.Topico;
import com.alura.foro.service.CursoService;
import com.alura.foro.service.PerfilService;
import com.alura.foro.service.TopicoService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/foro")
@AllArgsConstructor
public class TopicoController {
		
	private TopicoService topicoService;
	private CursoService cursoService;
	private PerfilService perfilService;
	

	/*********************************** 
	 * REST API POST
	 * Registrar nuevo Topico  
	 * END POINT :
	 * http://localhost:8080/foro/topico
	*************************************/
	@PostMapping("/topico")
	public ResponseEntity<ResponseTopico> saveTopico(@RequestBody Topico topico){
						
		topico.setCurso(cursoService.getCursoById(topico.getCurso().getId()));
		topico.setPerfil(perfilService.getPerfilById(topico.getPerfil().getId()));
		
		topico = topicoService.saveTopico(topico);
		
		ResponseTopico responseTopico = new ResponseTopico(
				topico.getId(), 
				topico.getTitulo(), topico.getMensaje(), 
				new ResponsePerfil(
						topico.getPerfil().getNombrecompleto(), 
						topico.getPerfil().getEmail(), 
						topico.getPerfil().getUsuario().getNombreusuario()), null);
		
		return new ResponseEntity<ResponseTopico>(responseTopico, HttpStatus.CREATED);
	}
	
	
	/************************************** 
	 * REST API GET
	 * Obtener todos los Topico 
	 * END POINT :
	 * http://localhost:8080/foro/topicos
	***************************************/
	@GetMapping("/topicos")
	public List<ResponseTopico> getAllTopico(){
		
		List<ResponseTopico> ListresponseTopico = new ArrayList<ResponseTopico>();
		List<Topico> topicos = topicoService.getAllTopicos();
		
		topicos.forEach(topico -> {
			ResponsePerfil perfil = new ResponsePerfil(
					topico.getPerfil().getNombrecompleto(), 
					topico.getPerfil().getEmail(), 
					topico.getPerfil().getUsuario().getNombreusuario());
			
			
			List<ResponseRespuestas> responseRespuestas = new ArrayList<ResponseRespuestas>();
			
			topico.getRespuestas().forEach(respuesta -> {
				
				responseRespuestas.add(new ResponseRespuestas(
						respuesta.getId(),
						respuesta.getMensaje(),
						respuesta.getIdtopico(),
						respuesta.getSolucion(), new ResponsePerfil(
								respuesta.getPerfil().getNombrecompleto(), 
								respuesta.getPerfil().getEmail(), 
								respuesta.getPerfil().getUsuario().getNombreusuario())));
			});
			
			
			
			ResponseTopico responseTopico = new ResponseTopico(
					topico.getId(), 
					topico.getTitulo(), 
					topico.getMensaje(), 
					perfil, responseRespuestas);
			
			ListresponseTopico.add(responseTopico);
		});
		
		return ListresponseTopico;
	}
	
	
	/******************************************* 
	 * REST API GET
	 * Obtener un Topico pasando el id 
	 * END POINT :
	 * http://localhost:8080/foro/topico/1
	********************************************/
	@GetMapping("/topico/{id}")
	public ResponseEntity<ResponseTopico> getTopicoById(@PathVariable("id") long id){
		
		Topico topico = topicoService.getTopicoById(id);
		ResponsePerfil perfil = new ResponsePerfil(
				topico.getPerfil().getNombrecompleto(), 
				topico.getPerfil().getEmail(), 
				topico.getPerfil().getUsuario().getNombreusuario());
		
		
		List<ResponseRespuestas> responseRespuestas = new ArrayList<ResponseRespuestas>();
		
		topico.getRespuestas().forEach(respuesta -> {
			
			responseRespuestas.add(new ResponseRespuestas(
					respuesta.getId(),
					respuesta.getMensaje(),
					respuesta.getIdtopico(),
					respuesta.getSolucion(), new ResponsePerfil(
							respuesta.getPerfil().getNombrecompleto(), 
							respuesta.getPerfil().getEmail(), 
							respuesta.getPerfil().getUsuario().getNombreusuario())));
		});
		
		
		ResponseTopico responseTopico = new ResponseTopico(
				topico.getId(), 
				topico.getTitulo(), 
				topico.getMensaje(), 
				perfil, responseRespuestas);
		
		
		return new ResponseEntity<ResponseTopico>(responseTopico, HttpStatus.OK);
	}
	
	
	/************************************************
	 * REST API GET 
	 * Obtener las Respuestas de un  
	 * Topico pasando el id del topico  REST API
	 * END POINT :
	 * http://localhost:8080/foro/topico/1/respuestas
	*************************************************/
	@GetMapping("/topico/{id}/respuestas")
	public ResponseEntity<Topico> getRespuestasTopicoById(@PathVariable("id") long id){
		
		return new ResponseEntity<Topico>(topicoService.getRespuestasTopicoById(id), HttpStatus.OK);
	}
	
	
	/************************************************
	 * REST API GET 
	 * Obtener las Respuestas de un  
	 * Topico pasando el id del topico
	 * END POINT :
	 * http://localhost:8080/foro/topico/1/respuestas
	*************************************************/
	//@GetMapping("/topico/{id}/respuestasbytopico")
	//public List<ResponseTopico> getRespuestasListTopicoById(@PathVariable("id") long id){	}
	
	
	
	/************************************************
	 * REST API PUT 
	 * Actualizar un topico por id
	 * END POINT :
	 * http://localhost:8080/foro/topico/1
	*************************************************/
	@PutMapping("/topico/{id}")
	public ResponseEntity<Topico> updateTopico(@PathVariable("id") long id
												  ,@RequestBody Topico topico){
		return new ResponseEntity<Topico>(topicoService.updateTopico(topico, id), HttpStatus.OK);
	}
	
	/************************************************
	 * REST API DELETE 
	 * borrar un topico por id
	 * END POINT :
	 * http://localhost:8080/foro/topico/1
	*************************************************/
	@DeleteMapping("/topico/{id}")
	public ResponseEntity<String> deleteTopico(@PathVariable("id") long id){
		
		// delete topico from DB
		topicoService.deleteTopico(id);
		
		return new ResponseEntity<String>("Topico deleted successfully!.", HttpStatus.OK);
	}
	
	
}
