package cu.iviera.detecsa;

public class Contacto {

    String nombre;
    String sap;
    String area;
    String cargo;
    String ubicacion;
    String fijo;
    String movil;
    String casa;
    String email;

    public Contacto() {
    }

    public Contacto(String nombre, String sap, String area, String cargo, String ubicacion, String fijo, String movil, String casa, String email) {
        this.nombre = nombre;
        this.sap = sap;
        this.area = area;
        this.cargo = cargo;
        this.ubicacion = ubicacion;
        this.fijo = fijo;
        this.movil = movil;
        this.casa = casa;
        this.email = email;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSap() {
        return sap;
    }

    public void setSap(String sap) {
        this.sap = sap;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFijo() {
        return fijo;
    }

    public void setFijo(String fijo) {
        this.fijo = fijo;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getCasa() {
        return casa;
    }

    public void setCasa(String casa) {
        this.casa = casa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}