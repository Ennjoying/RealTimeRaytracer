import java.util.ArrayList;

public class Quadric extends VectorObject {
	float a,b,c,d,e,f,g,h,i,j;
	
	private Matrix4 m = new Matrix4(); //Quadric Matrix
	
	public Quadric(Vec3f pos, Material material, float a, float b, float c, float d, float e, float f, float g, float h, float i, float radius) {
		// TODO Auto-generated constructor stub
		super(pos, material);
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.g = g;
		this.h = h;
		this.i = i;
		this.j = -radius;

		this.translate(pos.x,pos.y,pos.z);
	}
	
//	private Matrix4 translate(float x, float y, float z) {
//		Matrix4 matrix = new Matrix4();
//		return matrix.getTranslate(x, y, z);
//	}

	public void translate(float x, float y, float z) {
		Matrix4 translate = new Matrix4();
		transform(translate.getTranslate(-x, -y, -z));
	}
	
	public void scale(float scaleFactor) {
		Matrix4 scale = new Matrix4();
		transform(scale.getScale(1/scaleFactor));
	}
	
	//TODO funktioniert noch nicht richtig?
	public void rotateX(float angle) {
		Matrix4 rotateX = new Matrix4();
		transform(rotateX.getRotationX(-angle));
	}
	
	public void rotateY(float angle) {
		Matrix4 rotateY = new Matrix4();
		transform(rotateY.getRotationY(-angle));
	}
	
	public void rotateZ(float angle) {
		Matrix4 rotateZ = new Matrix4();
		transform(rotateZ.getRotationZ(-angle));
	}
	
	private void transform(Matrix4 transformation) {
		//quadric matrix  1,0,1,0,0,0,0,0,0,.1f
		m.m00 = a; m.m01 = d; m.m02 = e; m.m03 = g;
		m.m10 = d; m.m11 = b; m.m12 = f; m.m13 = h;
		m.m20 = e; m.m21 = f; m.m22 = c; m.m23 = i;
		m.m30 = g; m.m31 = h; m.m32 = i; m.m33 = j;
		
		Matrix4 transposed = transformation.transpose();
		
		// M-1^T * Q * M-1
		Matrix4 qStrich = transformation.multiply(m.multiply(transposed));
		
		a=qStrich.m00; d=qStrich.m01; e=qStrich.m02; g=qStrich.m03;
		d=qStrich.m10; b=qStrich.m11; f=qStrich.m12; h=qStrich.m13;
		e=qStrich.m20; f=qStrich.m21; c=qStrich.m22; i=qStrich.m23;
		g=qStrich.m30; h=qStrich.m31; i=qStrich.m32; j=qStrich.m33;
	}

	@Override
	public ArrayList<Intersection> intersect(Ray ray) {

		ArrayList<Intersection> Intersections = new ArrayList<>();
		Intersections.add(new Intersection(this,null, Float.POSITIVE_INFINITY));
		Intersections.add(new Intersection(this,null, Float.POSITIVE_INFINITY));

		Vec3f p = ray.pos;
		Vec3f v = ray.view;
		

		float aa = a*v.x*v.x + b*v.y*v.y + c*v.z*v.z + 2*d*v.x*v.y + 2*e*v.x*v.z + 2*f*v.y*v.z; 
		float bb = 2*a*v.x*p.x + 2*b*v.y*p.y + 2*c*v.z*p.z + 2*d*v.x*p.y + 2*d*v.y*p.x 
				   + 2*e*v.x*p.z + 2*e*v.z*p.x + 2*f*v.y*p.z + 2*f*v.z*p.y + 2*g*v.x + 2*h*v.y + 2*i*v.z;
		float cc = a*p.x*p.x + b*p.y*p.y + c*p.z*p.z + 2*d*p.x*p.y + 2*e*p.x*p.z + 2*f*p.z*p.y + 2*g*p.x + 2*h*p.y + 2*i*p.z + j;


		float k;
		if (aa != 0) {
			k = (float)(-bb - Math.signum(bb) * Math.sqrt(bb*bb-4*aa*cc))/2;
			if(cc/k <= k/aa){
				Intersections.get(0).s=cc/k;
				Intersections.get(1).s=k/aa;
			}else {
				Intersections.get(0).s=k/aa;
				Intersections.get(1).s=cc/k;
			}
		}

		return Intersections;
	}

	@Override
	public Vec3f getNormale(Intersection obj) {
		return new Vec3f(
				a*obj.vecIntersec.x + d*obj.vecIntersec.y + e*obj.vecIntersec.z + g,
				b*obj.vecIntersec.y + d*obj.vecIntersec.x + f*obj.vecIntersec.z + h,
				c*obj.vecIntersec.z + e*obj.vecIntersec.x + f*obj.vecIntersec.y + i);
	}

}
