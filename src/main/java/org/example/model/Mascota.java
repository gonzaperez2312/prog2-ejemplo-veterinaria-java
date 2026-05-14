package org.example.model;

public class Mascota {

    private Integer id;
    private String nombre;
    private String especie;
    private String propietario;
    private int edad;

    public Mascota(Integer id, String nombre, String especie, String propietario, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.propietario = propietario;
        this.edad = edad;
    }

    public Integer getId()          { return id; }
    public String getNombre()       { return nombre; }
    public String getEspecie()      { return especie; }
    public String getPropietario()  { return propietario; }
    public int getEdad()            { return edad; }
    public void setEdad(int edad)   { this.edad = edad; }

    @Override
    public String toString() {
        return "Mascota{ id=" + id
                + ", nombre='" + nombre + "'"
                + ", especie='" + especie + "'"
                + ", propietario='" + propietario + "'"
                + ", edad=" + edad + " }";
    }
}
