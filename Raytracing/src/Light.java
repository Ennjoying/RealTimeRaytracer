import java.util.ArrayList;
import java.util.Random;

public class Light {
	Vec3f pos;
	Vec3f direction = new Vec3f(0,0,-40); //is pointing to the middle of the picture
	float intensity;
	Vec3f color;
	//Vec3f[] lightPoints = new Vec3f[5]; // 5 points on the light to make the soft shadows
	float radius; //radius of the light ball
	int lightPointNr; //amount of lightPoints we want to create
	//Vec3f[] lightPoints = new Vec3f[lightPointNr];
	ArrayList<Vec3f> lightPoints = new ArrayList<>();
	
	public Light(Vec3f pos, Vec3f color,float intensity, float radius, int lightPointNr) {
		this.pos = pos;
		this.color = color;
		this.intensity = intensity;
		
		this.radius = radius;
		this.lightPointNr = lightPointNr + 1; //+1 because we add the light position as a point as well
		setLightPointArray();
		
		
//		lightPoints[0] = pos;
//		lightPoints[1] = pos.add(new Vec3f(2f,0,0)); 
//		lightPoints[2] = pos.add(new Vec3f(2f,0,0)); 
//		lightPoints[3] = pos.add(new Vec3f(0,2f,0)); 
//		lightPoints[4] = pos.add(new Vec3f(0,-2f,0)); 
	}
	
	
	
	//get a truly random vector on the surface of the light ball
	private Vec3f getRandomPointOnLightBall() {
		Random r = new Random();
		float x = r.nextFloat()+r.nextInt()* (r.nextBoolean() ? -1 : 1);
		float y = r.nextFloat()+r.nextInt()* (r.nextBoolean() ? -1 : 1);
		float z = r.nextFloat()+r.nextInt()* (r.nextBoolean() ? -1 : 1);
		Vec3f randomVector = new Vec3f(x,y,z).normalize().multiply(radius);
		
		return randomVector;
	}
	
	
	
	public void setLightPointArray() {
		
		for(int i=0; i<lightPointNr-1; i++) {
			lightPoints.add(getRandomPointOnLightBall());
			//System.out.println("x: " + lightPoints.get(i).x + " y: " + lightPoints.get(i).y + " z: " + lightPoints.get(i).z);
			
		}
		lightPoints.add(pos);
		
	}
	
	
	public int calcLight(Intersection obj){


		Vec3f n = obj.obj.getNormale(obj).normalize();
		Vec3f r = getDirection(obj.vecIntersec);
		float distance = r.length();
		r = r.normalize();

		Vec3f rgbVec = obj.obj.material.color;
		float diffuseLight = n.scalar(r.multiply(-1));

		//float degree = (float) Math.acos(brightness/(normale.length() * r.length()));
		//System.out.println("brightness: " + brightness + " degree: " + degree);


		Vec3f ultimateLightColor = rgbVec.multiply(diffuseLight/(distance*distance+1)*intensity);
		//Vec3f ultimateLightColor = rgbVec.multiplyVector(diffuseLight*light.intensity);

		ultimateLightColor.setX(Math.min(255, Math.max(0, ultimateLightColor.x)));
		ultimateLightColor.setY(Math.min(255, Math.max(0, ultimateLightColor.y)));
		ultimateLightColor.setZ(Math.min(255, Math.max(0, ultimateLightColor.z)));

		return ((int)ultimateLightColor.x <<16) | ((int)ultimateLightColor.y <<8) | (int)ultimateLightColor.z;

	}

	public Vec3f getPos() {
		return pos;
	}
	
	public void setPos(Vec3f pos) {
		this.pos = pos;
	}

	public Vec3f cookTorrance(Intersection intersec, Vec3f matColor){

		float distance = intersec.vecIntersec.length();
		Vec3f light = this.pos.subtractVector(intersec.vecIntersec).normalize();

		Vec3f N = intersec.parentObj.getNormale(intersec).normalize();
		Vec3f view = intersec.vecIntersec.multiply(-1).normalize();
		Vec3f H = view.addVector(light).normalize();

		float r = intersec.obj.material.roughness;
		float m = intersec.obj.material.metalness;
		Vec3f F0 = new Vec3f((1-m)*0.04f+m); //new Vec3f(0.04f);

		float nv= Math.max(N.scalar(view),0);
		float nl= Math.max(N.scalar(light),0);
		float nh= Math.max(N.scalar(H),0);

		Vec3f F = getFresnel(F0, nv);
		float D = getDistribution(r,nh);
		float G = getGeometry(r,nv,nl);

		Vec3f ks = F.multiply(D*G).clamp(0,1);
		Vec3f kd = new Vec3f(1,1,1).subtractVector(ks).multiply(1 - m);

		Vec3f farbe = new Vec3f(matColor.x, matColor.y, matColor.z);
		Vec3f lightColor = new Vec3f(color.x, color.y, color.z);
		farbe = kd.multiplyWithVector(farbe).addVector(ks).multiplyWithVector(lightColor).multiply(this.intensity/*/(distance*distance+1)*/ * nl);
		farbe = farbe.clamp(0,1);


		return farbe;
	}

	public Vec3f cookTorranceTransmission(Intersection intersec, Vec3f matColor){
		float distance = intersec.vecIntersec.length();
		Vec3f light = this.pos.subtractVector(intersec.vecIntersec).normalize();

		Vec3f N = intersec.parentObj.getNormale(intersec).normalize();
		Vec3f view = intersec.vecIntersec.multiply(-1).normalize();
		Vec3f H = view.addVector(light).normalize();

		float r = intersec.obj.material.roughness;
		float m = intersec.obj.material.metalness;
		Vec3f F0 = new Vec3f((1-m)*0.04f+m); //new Vec3f(0.04f);

		float nv= Math.max(N.scalar(view),0);
		float nl= Math.max(N.scalar(light),0);
		float nh= Math.max(N.scalar(H),0);

		Vec3f F = getFresnel(F0, nv);
		float D = getDistribution(r,nh);
		float G = getGeometry(r,nv,nl);

		Vec3f ks = F.multiply(D*G).clamp(0,1);
		Vec3f kd = new Vec3f(1,1,1).subtractVector(ks).multiply(1 - m);

		//Vec3f farbe = new Vec3f(intersec.obj.material.color.x, intersec.obj.material.color.y, intersec.obj.material.color.z);
		Vec3f farbe= new Vec3f(matColor.x, matColor.y, matColor.z);
		Vec3f lightColor = new Vec3f(color.x, color.y, color.z);

		farbe = lightColor.multiply(this.intensity).multiply(nl).multiplyWithVector(farbe.multiplyWithVector((new Vec3f(-1).subtractVector(F)).addVector(ks) ) );

		return farbe;
	}

	public float getGeometry(float r, float nv, float nl){
		return nv / (nv * (1-r/2) + r/2) * nl / (nl*(1 - r/2) + r/2);
	}

	public float getDistribution(float r, float nh){
		return (float)( r*r / ( Math.PI * Math.pow(((nh*nh) * (r*r - 1)+1),2) ) );
	}
	public Vec3f getFresnel(Vec3f F0, float nv){
		return F0.addVector( (new Vec3f(1).subtractVector(F0)) .multiply((float)Math.pow((1 - nv),5)));
	}

	
	//TODO doesn't work quite right -> why is there no shadowRay inside the cone?
	/*public boolean isInLightCone(Vec3f ray) {
		float angleBetweenRays = direction.getAngle(ray.multiply(-1));
		if(angleBetweenRays < angle/2) return true;
		System.out.println(angleBetweenRays);
		return false;
	}*/
	
//	public float getLightAngle() {
//		return angle;
//	}
//	
//	public void setLightAngle(float angle) {
//		this.angle = angle;
//	}
//	
//	//TODO doesn't work quite right -> why is there no shadowRay inside the cone?
//	public boolean isInLightCone(Vec3f ray) {
//		float angleBetweenRays = direction.getAngle(ray.multiply(-1));
//		if(angleBetweenRays < angle/2) return true;
//		System.out.println(angleBetweenRays);
//		return false;
//	}
//	
	
	/**
	*gibt den Richtungsvektor des Lichts
	*zu einem Punkt auf der Oberfläche des Objekts wieder
	**/
	public Vec3f getDirection(Vec3f posOnObj) {
		return posOnObj.subtractVector(this.pos);
	}

}
