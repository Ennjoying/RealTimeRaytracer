public class Ray {
    Vec3f pos;
    Vec3f view;

    int color;

    public Ray(Vec3f pos, Vec3f view) {
        this.pos = pos;
        this.view = view;
        this.color = 0x000000;
    }

    // r = v – 2(n·v)n  formel
    //auslagern in ray class damit man ray.reflect aufrufen kann
    //ray stückweit in view bewegen
    //ray.pos = intersec.vecIntersec;
    //ray.view =

    public Ray reflect(Intersection intersec){

        Vec3f N = intersec.parentObj.getNormale(intersec).normalize();
        Vec3f view = intersec.vecIntersec.multiply(-1).normalize();
        float nv= Math.max(N.scalar(view),0);

        Vec3f newView = N.multiply(2*nv);
        newView = view.subtractVector(newView);
        return new Ray(intersec.vecIntersec.addVector(newView.multiply(0.1f)),newView);
    }

    public Ray refract(Intersection intersec){

        Vec3f n = intersec.parentObj.getNormale(intersec).normalize();

        Vec3f v1 = this.view.normalize();

        float i;
        if (v1.scalar(n) <= 0){ //gibt an ob man in ein objekt reingeht oder rausgeht.
            i = SUBSTANCE.air.getBrechungsIndex() / intersec.obj.material.SUBSTANCE.getBrechungsIndex();
        } else {
            n.multiply(-1);
            i = intersec.obj.material.SUBSTANCE.getBrechungsIndex() / SUBSTANCE.air.getBrechungsIndex();
        }

        float a = v1.multiply(-1f).normalize().scalar(n);
        float b = (float)Math.sqrt(1- i*i*(1-a*a));

        Vec3f hmm = n.multiply(i*a-b);
        Vec3f v2 = v1.multiply(i).addVector(hmm);
        return new  Ray(intersec.vecIntersec.addVector(v2.multiply(0.1f)), v2);
    }
}
