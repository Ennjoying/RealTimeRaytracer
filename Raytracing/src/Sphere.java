import java.util.ArrayList;

public class Sphere extends VectorObject{
	float radius;

	//DEMONDISTORTION dd;

	public Sphere(Vec3f pos, Material material, float radius) {
		super(pos, material);
		this.radius = radius;
	}

	public Sphere(Sphere redSphere) {
		this.pos = redSphere.pos;
		this.material = redSphere.material;
		this.radius = redSphere.radius;
	}

	/*public Sphere(Vec3f pos, Vec3f view, Material material, float radius, DEMONDISTORTION dd) {
		super(pos, view, material);
		this.radius = radius;
		this.dd = dd;
	}*/

	public ArrayList<Intersection> intersect(Ray ray){

		ArrayList<Intersection> Intersections = new ArrayList<>();
		Intersections.add(new Intersection(this,null, Float.POSITIVE_INFINITY));
		Intersections.add(new Intersection(this,null, Float.POSITIVE_INFINITY));

		Vec3f p = ray.pos;
		Vec3f v = ray.view;
		Vec3f m = this.pos; //keine Verschiebung des Mittelpunktes
		float r = this.radius;

		float a = v.x*v.x + v.y*v.y +  v.z*v.z;
		float b = 2*p.x*v.x-2*v.x*m.x + 2*p.y*v.y-2*v.y*m.y + 2*p.z*v.z-2*v.z*m.z;
		float c = p.x*p.x + p.y*p.y + p.z*p.z - 2*p.x*m.x - 2*p.y*m.y - 2*p.z*m.z
				+ m.x*m.x + m.y*m.y + m.z*m.z - r*r;


		float k;
		if (a != 0) {
			k = (float)(-b - Math.signum(b) * Math.sqrt(b*b-4*a*c))/2;
			if(c/k <= k/a){
				Intersections.get(0).s=c/k;
				Intersections.get(1).s=k/a;
			}else {
				Intersections.get(0).s=k/a;
				Intersections.get(1).s=c/k;
			}
		}

		return Intersections;
	}

	@Override
	public Vec3f getNormale(Intersection intersecObj) {
		return intersecObj.vecIntersec.subtractVector(this.pos);
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

}