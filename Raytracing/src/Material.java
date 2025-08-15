public class Material {

    Vec3f color;
    float roughness;
    float metalness;
    float reflectivity;
    float transmission;
    SUBSTANCE SUBSTANCE;


    public Material(Vec3f color, float roughness, float metalness, float transmission, SUBSTANCE SUBSTANCE) {
        this.color = color;
        this.roughness = roughness;
        this.metalness = metalness;
        this.reflectivity = 1-roughness;
        this.transmission = transmission;
        this.SUBSTANCE = SUBSTANCE;
    }



}
