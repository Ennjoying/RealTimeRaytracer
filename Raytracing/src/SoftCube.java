import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SoftCube extends VectorObject{

	private float aa;
	private float bb;
	private float cc;
	private float dd;
	private float radius;
	private double EPSILON = 1e-6; // Genauigkeit

    boolean isINTERSECTION;


	
	public SoftCube(Vec3f pos, Material material, float radius) {
		super(pos, material);
		this.radius = radius;
		
	}


    public ArrayList<Double> solveEquation(Vec3f p, Vec3f v, float r) {
        ArrayList<Double> solutions = new ArrayList<>();

        // Startwerte für s
        double s = 0.0;
        double deltaS = 0.01;

        // Schleife über s-Werte
        while (s <= 1.0) {
            double f = Math.pow(Math.abs(p.x + s * v.x), 3) + Math.pow(Math.abs(p.y + s * v.y), 3)
                    + Math.pow(Math.abs(p.z + s * v.z), 3) - Math.pow(r, 3);

            // Überprüfung auf Näherungslösungen
            if (Math.abs(f) < EPSILON) {
                solutions.add(s);
            }

            // Newton-Raphson-Verfahren
            double df = 3 * (Math.pow(p.x + s * v.x, 2) * v.x * Math.signum(p.x + s * v.x)
                    + Math.pow(p.y + s * v.y, 2) * v.y * Math.signum(p.y + s * v.y)
                    + Math.pow(p.z + s * v.z, 2) * v.z * Math.signum(p.z + s * v.z));

            s -= f / df; // Newton-Raphson-Schritt

            // Schrittweite anpassen, um Konvergenz sicherzustellen
            if (Math.abs(f) > Math.abs(f - (f / df))) {
                deltaS /= 2.0;
            }

            s += deltaS; // Nächster s-Wert
        }
        Collections.sort(solutions);
        return solutions;
    }
	
	
	private ArrayList<Intersection> cubicIntersections(float a, float b, float c, float d) {
		double[] result;
        if (a != 1) {
            d = d / a;
            c = c / a;
            b = b / a;
        }

        double p = c / 3 - b * b / 9;
        double q = b * b * b / 27 - b * c / 6 + d / 2;
        double D = p * p * p + q * q;

        if (Double.compare(D, 0) >= 0) {
            if (Double.compare(D, 0) == 0) {
                double r = Math.cbrt(-q);
                result = new double[2];
                result[0] = 2 * r;
                result[1] = -r;
            } else {
                double r = Math.cbrt(-q + Math.sqrt(D));
                double s = Math.cbrt(-q - Math.sqrt(D));
                result = new double[1];
                result[0] = r + s;
            }
        } else {
            double ang = Math.acos(-q / Math.sqrt(-p * p * p));
            double r = 2 * Math.sqrt(-p);
            result = new double[3];
            for (int k = -1; k <= 1; k++) {
                double theta = (ang - 2 * Math.PI * k) / 3;
                result[k + 1] = r * Math.cos(theta);
            }
        }
        
        ArrayList<Intersection> Intersections = new ArrayList<>();
        
        //sort from smallest intersection to biggest
        Arrays.sort(result);
        
        for (int i = 0; i < result.length; i++) {
            Intersections.add(new Intersection(this,null,(float)result[i] - a / 3));
            //System.out.println(result[i]);
        }
        
		return Intersections;
	}
	

	@Override
	public ArrayList<Intersection> intersect(Ray ray) {
		
		Vec3f pp = ray.pos;
		Vec3f v = ray.view;
		
//		System.out.println("p: " + pp);
//		System.out.println("v: " + v);
		//ArrayList<Double> result = solveEquation(pp,v,radius);
		
		//s^3*a + s^2*b + s*c + d
		
		bb = 3*(pp.x*v.x*v.x + pp.y*v.y*v.y + pp.z*v.z*v.z);
		cc = 3*(pp.x*pp.x*v.x + pp.y*pp.y*v.y + pp.z*pp.z*v.z);
		dd = (pp.x*pp.x*pp.x + pp.y*pp.y*pp.y + pp.z*pp.z*pp.z)-radius*radius*radius;
		aa = v.x*v.x*v.x + v.y*v.y*v.y + v.z*v.z*v.z;
		
        
		return cubicIntersections(aa,bb,cc,dd);
		
		
//		ArrayList<Double> result = cubicIntersections();
//		ArrayList<Intersection> Intersections = new ArrayList();
//		
//		for (int i = 0; i < result.size(); i++) {
//			
//            Intersections.add(new Intersection(this,null, result.get(i).floatValue()));
//        }
//		
//		return Intersections;
	}
    /* //region Raymarching

    Vec3f R;

    boolean isDemonDistorted = false;
    DEMONDISTORTION demondistortion;

    public SoftCube(Vec3f pos, Vec3f view, Material material, float radius, Vec3f r) {
        super(pos, view, material);
        this.radius = radius;
        R = r;
    }


    @Override
    public ArrayList<Intersection> intersect(Ray ray) {
        //SDF function & raymarching algorithm2
        ArrayList<Intersection> intersections = new ArrayList<>();

        Intersection intersec = rayMarch(ray, pos.length()+Math.abs(R.length()));

        intersec.vecIntersec = ray.pos.addVector(ray.view.multiply(intersec.s));
        intersec.parentObj = intersec.obj;
        intersections.add(intersec);

        return intersections;
    }

    private Intersection rayMarch(Ray ray, float maxPossibleDistance){
        Vec3f punkt = new Vec3f(ray.pos);

        Intersection intersec = new Intersection(this,null,Float.POSITIVE_INFINITY);
        float s=0;
        float abstand;
        float epsilon = 0.01f;
        while(s < maxPossibleDistance){

            abstand = SDF(punkt);
            s+= abstand;
            if(abstand < epsilon){
                intersec.s = s;
                return intersec;
            }
            punkt = punkt.addVector(ray.view.multiply(abstand));
        }
        return intersec;
    }
    private float SDF(float x, float y, float z){
        return SDF( new Vec3f (x,y,z));
    }
    private float SDF(Vec3f p){
        float r = 0f;


        //change p for dämonenverzerrung vor allem anderen

        if (isDemonDistorted) p = demondistortion.demonDistortion(p, R.y,1f);

        Vec3f q = p.subtractVector(pos).abs().subtractVector(R);
        return q.max(0).length()+Math.min(q.maxComp(),0) - r;
    }
    public void setDemonDistortion(AxisSelector selectedAxis, Vec3f posToDistortAround){
        isDemonDistorted = true;
        demondistortion = new DEMONDISTORTION(selectedAxis, posToDistortAround);
    }

    //endregion */
	@Override
	public Vec3f getNormale(Intersection intersec) {
		
		return new Vec3f(intersec.vecIntersec.x*intersec.vecIntersec.x, intersec.vecIntersec.y*intersec.vecIntersec.y, intersec.vecIntersec.z*intersec.vecIntersec.z);
	}

}
