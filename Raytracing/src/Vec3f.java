public class Vec3f {
    float x;
    float y;
    float z;

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f(float xyz){
        this.x = xyz;
        this.y = xyz;
        this.z = xyz;
    }

    public Vec3f(Vec3f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public Vec3f crossProduct(Vec3f other){
        Vec3f out = new Vec3f(1,1,1);
        out.x = this.y * other.z - this.z * other.y;
        out.y = this.z * other.x - this.x * other.z;
        out.z = this.x * other.y - this.y * other.x;

        if(out.x == -0){
            out.x = 0;
        }
        if(out.y == -0){
            out.y = 0;
        }
        if(out.z == -0){
            out.z = 0;
        }

        return out;
    }

    public Vec3f normalize(){
        double betrag = Math.sqrt(Math.pow(this.x,2) + Math.pow(this.y,2) + Math.pow(this.z,2));
        Vec3f tmp = new Vec3f((float)(this.x/betrag),(float)(this.y/betrag),(float)(this.z/betrag));
        return tmp;
    }

    public Vec3f clamp(float min, float max){
        return new Vec3f(
                Math.min(max, Math.max(min, x)),
                Math.min(max, Math.max(min, y)),
                Math.min(max, Math.max(min, z)));
    }

    public Vec3f max(float lowerBoundary){
        return new Vec3f(
                Math.max(x,lowerBoundary),
                Math.max(y,lowerBoundary),
                Math.max(z,lowerBoundary));
    }

    public Vec3f pow(float pow){
        return new Vec3f(
                (float)Math.pow( x , pow ),
                (float)Math.pow( y , pow ),
                (float)Math.pow( z , pow ));
    }

    public Vec3f abs(){
        return new Vec3f(
                Math.abs(x),
                Math.abs(y),
                Math.abs(z));
    }
    public float maxComp(){
        return Math.max(Math.max(x,y), z);
    }

    public Vec3f add(float num) {
        return new Vec3f(x + num, y + num, z + num);
    }

    public Vec3f addVector(Vec3f vector) {
		return new Vec3f(x + vector.x , y + vector.y , z + vector.z);
	}

	public Vec3f subtractVector(Vec3f vector) {
		return new Vec3f(x - vector.x, y - vector.y, z - vector.z);
	}

    public Vec3f subtract(float num){
        return new Vec3f(this.x-num, this.y-num, this.z-num);
    }

    public Vec3f multiplyWithMatrix4(Matrix4E m4){
        Vec3f tmp = new Vec3f(0);

        tmp.x = m4.values[0]*x + m4.values[4]*y + m4.values[8]*z + m4.values[12]*1;
        tmp.y = m4.values[1]*x + m4.values[5]*y + m4.values[9]*z + m4.values[13]*1;
        tmp.z = m4.values[2]*x + m4.values[6]*y + m4.values[10]*z + m4.values[14]*1;

        return tmp;
    }
    /*	x=	0 1 2  3

        y=0 0 4 8  12
    * 	y=1 1 5 9  13
    * 	y=2 2 6 10 14
    * 	y=3 3 7 11 15
    * */

	public Vec3f multiply(float scalar) {
		return new Vec3f(x * scalar, y * scalar, z * scalar);
	}

    public Vec3f multiplyWithVector(Vec3f vec){
        return new Vec3f(this.x*vec.x, this.y*vec.y, this.z*vec.z);
    }
	
	public float scalar(Vec3f vector) {
		return this.x * vector.x + this.y * vector.y + this.z * vector.z;
	}
	
	public float length() {
		return (float) Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
	}
	
	public float getAngle(Vec3f vector) {
		float cosA = this.scalar(vector)/(this.length()*vector.length());
		return (float) Math.toDegrees(Math.acos(cosA));
	}

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }
    
}
