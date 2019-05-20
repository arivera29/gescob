package com.are.gescob.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	private Collection collection;
	@NotEmpty
	private String numCamp;  // numero de campaña
	@NotEmpty
	private java.util.Date fechaEntrega;
	@NotEmpty
	private String unicom;
	@NotEmpty
	private String nic;
	@NotEmpty
	private String nis;
	@NotEmpty
	private String departamento;
	@NotEmpty
	private String municipio;
	@NotEmpty
	private String corregimiento;
	private String barrio;
	private String nomVia;
	private String nomCalle;
	private String duplicador;
	private String nroPuerta;
	private String cgv;
	private String refDir;
	@NotEmpty
	private String cliente;
	@NotEmpty
	private String cedula;
	private String telefono;
	private String tarifa;
	private String estadoSuministro;
	private String ruta;
	private String itim;
	private String aol;
	private String medidor;
	private String tipoMedidor;
	private String marcaMedidor;
	@NotEmpty
	private Double deudaEnergia;
	@NotEmpty
	private Double deudaTerceros;
	@NotEmpty
	private Double deudaFinanciada;
	@NotEmpty
	private Long facturasVencidas;
	@NotEmpty
	private Long facturasAcordadas;
	private String pagoDatafono;
	private java.util.Date createdDate;
	@ManyToOne
	private User createUser;
	private Long countVisits;
	private Long countCanceled;
	private Long countVisited;
	@ManyToOne
	private Account account;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Collection getCollection() {
		return collection;
	}
	public void setCollection(Collection collection) {
		this.collection = collection;
	}
	public String getNumCamp() {
		return numCamp;
	}
	public void setNumCamp(String numCamp) {
		this.numCamp = numCamp;
	}
	public java.util.Date getFechaEntrega() {
		return fechaEntrega;
	}
	public void setFechaEntrega(java.util.Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	
	public String getUnicom() {
		return unicom;
	}
	public void setUnicom(String unicom) {
		this.unicom = unicom;
	}
	public String getNic() {
		return nic;
	}
	public void setNic(String nic) {
		this.nic = nic;
	}
	public String getNis() {
		return nis;
	}
	public void setNis(String nis) {
		this.nis = nis;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getCorregimiento() {
		return corregimiento;
	}
	public void setCorregimiento(String corregimiento) {
		this.corregimiento = corregimiento;
	}
	public String getBarrio() {
		return barrio;
	}
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}
	public String getNomVia() {
		return nomVia;
	}
	public void setNomVia(String nomVia) {
		this.nomVia = nomVia;
	}
	public String getNomCalle() {
		return nomCalle;
	}
	public void setNomCalle(String nomCalle) {
		this.nomCalle = nomCalle;
	}
	public String getDuplicador() {
		return duplicador;
	}
	public void setDuplicador(String duplicador) {
		this.duplicador = duplicador;
	}
	public String getNroPuerta() {
		return nroPuerta;
	}
	public void setNroPuerta(String nroPuerta) {
		this.nroPuerta = nroPuerta;
	}
	public String getCgv() {
		return cgv;
	}
	public void setCgv(String cgv) {
		this.cgv = cgv;
	}
	public String getRefDir() {
		return refDir;
	}
	public void setRefDir(String refDir) {
		this.refDir = refDir;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getTarifa() {
		return tarifa;
	}
	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
	}
	public String getEstadoSuministro() {
		return estadoSuministro;
	}
	public void setEstadoSuministro(String estadoSuministro) {
		this.estadoSuministro = estadoSuministro;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getItim() {
		return itim;
	}
	public void setItim(String itim) {
		this.itim = itim;
	}
	public String getAol() {
		return aol;
	}
	public void setAol(String aol) {
		this.aol = aol;
	}
	public String getMedidor() {
		return medidor;
	}
	public void setMedidor(String medidor) {
		this.medidor = medidor;
	}
	public String getTipoMedidor() {
		return tipoMedidor;
	}
	public void setTipoMedidor(String tipoMedidor) {
		this.tipoMedidor = tipoMedidor;
	}
	public String getMarcaMedidor() {
		return marcaMedidor;
	}
	public void setMarcaMedidor(String marcaMedidor) {
		this.marcaMedidor = marcaMedidor;
	}
	public Double getDeudaEnergia() {
		return deudaEnergia;
	}
	public void setDeudaEnergia(Double deudaEnergia) {
		this.deudaEnergia = deudaEnergia;
	}
	public Double getDeudaTerceros() {
		return deudaTerceros;
	}
	public void setDeudaTerceros(Double deudaTerceros) {
		this.deudaTerceros = deudaTerceros;
	}
	public Double getDeudaFinanciada() {
		return deudaFinanciada;
	}
	public void setDeudaFinanciada(Double deudaFinanciada) {
		this.deudaFinanciada = deudaFinanciada;
	}
	public Long getFacturasVencidas() {
		return facturasVencidas;
	}
	public void setFacturasVencidas(Long facturasVencidas) {
		this.facturasVencidas = facturasVencidas;
	}
	public Long getFacturasAcordadas() {
		return facturasAcordadas;
	}
	public void setFacturasAcordadas(Long facturasAcordadas) {
		this.facturasAcordadas = facturasAcordadas;
	}
	public String getPagoDatafono() {
		return pagoDatafono;
	}
	public void setPagoDatafono(String pagoDatafono) {
		this.pagoDatafono = pagoDatafono;
	}
	public java.util.Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}
	public User getCreateUser() {
		return createUser;
	}
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}
	public Long getCountVisits() {
		return countVisits;
	}
	public void setCountVisits(Long countVisits) {
		this.countVisits = countVisits;
	}
	public Long getCountCanceled() {
		return countCanceled;
	}
	public void setCountCanceled(Long countCanceled) {
		this.countCanceled = countCanceled;
	}
	public Long getCountVisited() {
		return countVisited;
	}
	public void setCountVisited(Long countVisited) {
		this.countVisited = countVisited;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
	
	
	
	
}
