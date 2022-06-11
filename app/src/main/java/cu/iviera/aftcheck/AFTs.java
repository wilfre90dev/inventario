package cu.iviera.aftcheck;

public class AFTs {
    String inventario;
    String inmovilizado;
    String centroCosto;
    String area;
    String descripcion;
    int isChecked;

    public AFTs() {
    }

    public AFTs(String inventario, String inmovilizado, String centroCosto, String area, String descripcion, int isChecked) {
        this.inventario = inventario;
        this.inmovilizado = inmovilizado;
        this.centroCosto = centroCosto;
        this.area = area;
        this.descripcion = descripcion;
        this.isChecked= isChecked;
    }

    public String getInventario() {
        return inventario;
    }

    public void setInventario(String inventario) {
        this.inventario = inventario;
    }

    public String getInmovilizado() {
        return inmovilizado;
    }

    public void setInmovilizado(String inmovilizado) {
        this.inmovilizado = inmovilizado;
    }

    public String getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(String centroCosto) {
        this.centroCosto = centroCosto;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int isChecked() {
        return isChecked;
    }

    public void setChecked(int checked) {
        isChecked = checked;
    }
}
