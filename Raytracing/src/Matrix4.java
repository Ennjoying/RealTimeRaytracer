

//Alle Operationen ändern das Matrixobjekt selbst und geben das eigene Matrixobjekt zurück
//Dadurch kann man Aufrufe verketten, z.B.
//Matrix4 m = new Matrix4().scale(5).translate(0,1,0).rotateX(0.5f);
public class Matrix4 {
	float m00, m01, m02, m03,
		  m10, m11, m12, m13,
		  m20, m21, m22, m23,
		  m30, m31, m32, m33;
	

	public Matrix4() {
		// TODO mit der Identitätsmatrix initialisieren
		  m00 = 1; m01 = 0; m02 = 0; m03 = 0;
		  m10 = 0; m11 = 1; m12 = 0; m13 = 0;
		  m20 = 0; m21 = 0; m22 = 1; m23 = 0;
		  m30 = 0; m31 = 0; m32 = 0; m33 = 1;
	}

	public Matrix4(Matrix4 copy) {
		// TODO neues Objekt mit den Werten von "copy" initialisieren
		  m00 = copy.m00; m01 = copy.m01; m02 = copy.m02; m03 = copy.m03;
		  m10 = copy.m10; m11 = copy.m11; m12 = copy.m12; m13 = copy.m13;
		  m20 = copy.m20; m21 = copy.m21; m22 = copy.m22; m23 = copy.m23;
		  m30 = copy.m30; m31 = copy.m31; m32 = copy.m32; m33 = copy.m33;
	}

	//TODO die Matrix mit nur near und far benutzen
	public Matrix4(float near, float far , float b, float h) {
		// TODO erzeugt Projektionsmatrix mit Abstand zur nahen Ebene "near" und Abstand zur fernen Ebene "far", ggf. weitere Parameter hinzufügen
		  m00 = (2*near)/b; m01 = 0; 		  m02 = 0;						 m03 = 0;
		  m10 = 0; 			m11 = (2*near)/h; m12 = 0; 						 m13 = 0;
		  m20 = 0; 			m21 = 0; 		  m22 = (-far-near)/ (far-near); m23 = (-2*near*far)/(far-near);
		  m30 = 0; 			m31 = 0; 		  m32 = -1; 					 m33 = 0;
	}

	public Matrix4 multiply(Matrix4 other) {
		// TODO hier Matrizenmultiplikation "this = other * this" einfügen
		 Matrix4 copy = new Matrix4(this);
		  //erste Zeile
		  m00 = other.m00*copy.m00+other.m01*copy.m10+other.m02*copy.m20+other.m03*copy.m30; 
		  m01 = other.m00*copy.m01+other.m01*copy.m11+other.m02*copy.m21+other.m03*copy.m31;
		  m02 = other.m00*copy.m02+other.m01*copy.m12+other.m02*copy.m22+other.m03*copy.m32;
		  m03 = other.m00*copy.m03+other.m01*copy.m13+other.m02*copy.m23+other.m03*copy.m33;
		  
		  //zweite Zeile
		  m10 = other.m10*copy.m00+other.m11*copy.m10+other.m12*copy.m20+other.m13*copy.m30; 
		  m11 = other.m10*copy.m01+other.m11*copy.m11+other.m12*copy.m21+other.m13*copy.m31;
		  m12 = other.m10*copy.m02+other.m11*copy.m12+other.m12*copy.m22+other.m13*copy.m32;
		  m13 = other.m10*copy.m03+other.m11*copy.m13+other.m12*copy.m23+other.m13*copy.m33;
		  
		  //dritte Zeile
		  m20 = other.m20*copy.m00+other.m21*copy.m10+other.m22*copy.m20+other.m23*copy.m30; 
		  m21 = other.m20*copy.m01+other.m21*copy.m11+other.m22*copy.m21+other.m23*copy.m31;
		  m22 = other.m20*copy.m02+other.m21*copy.m12+other.m22*copy.m22+other.m23*copy.m32;
		  m23 = other.m20*copy.m03+other.m21*copy.m13+other.m22*copy.m23+other.m23*copy.m33;
		 
		  //vierte Zeile
		  m30 = other.m30*copy.m00+other.m31*copy.m10+other.m32*copy.m20+other.m33*copy.m30; 
		  m31 = other.m30*copy.m01+other.m31*copy.m11+other.m32*copy.m21+other.m33*copy.m31;
		  m32 = other.m30*copy.m02+other.m31*copy.m12+other.m32*copy.m22+other.m33*copy.m32;
		  m33 = other.m30*copy.m03+other.m31*copy.m13+other.m32*copy.m23+other.m33*copy.m33;
		  
		return this;
	}
	
	public Matrix4 transpose() {
		/*
		  from:
		  m00, m01, m02, m03,
		  m10, m11, m12, m13,
		  m20, m21, m22, m23,
		  m30, m31, m32, m33;
		  
		  to:
		  m00, m10, m20, m30,
		  m01, m11, m21, 031,
		  m02, m12, m22m m32,
		  m03, m13, m23, m33;
		 */
		Matrix4 t = new Matrix4();
		t.m00=m00; t.m01=m10; t.m02=m20; t.m03=m30;
		t.m10=m01; t.m11=m11; t.m12=m21; t.m13=m31;
		t.m20=m02; t.m21=m12; t.m22=m22; t.m23=m32;
		t.m30=m03; t.m31=m13; t.m32=m23; t.m33=m33;
		
		return t;
	}
	
	
	// Inverse translation by x, y and z
	public Matrix4 getTranslate(float x, float y, float z) {
		  m00 = 1; m01 = 0; m02 = 0; m03 = x;
		  m10 = 0; m11 = 1; m12 = 0; m13 = y;
		  m20 = 0; m21 = 0; m22 = 1; m23 = z;
		  m30 = 0; m31 = 0; m32 = 0; m33 = 1;
		 return this;
	}
	
	// Inverse scaling by float scaleFactor
	public Matrix4 getScale(float scaleFactor) {
		  m00 = scaleFactor; m01 = 0; m02 = 0; m03 = 0;
		  m10 = 0; m11 = scaleFactor; m12 = 0; m13 = 0;
		  m20 = 0; m21 = 0; m22 = scaleFactor; m23 = 0;
		  m30 = 0; m31 = 0; m32 = 0; m33 = 1;
		  return this;
	}
	
	//Inverse rotation by x
	public Matrix4 getRotationX(float angle) {
		  m00 = 1; m01 = 0; m02 = 0; m03 = 0;
		  m10 = 0; m11 = (float)Math.cos(angle); m12 = (float)-Math.sin(angle); m13 = 0;
		  m20 = 0; m21 = (float)Math.sin(angle); m22 = (float)Math.cos(angle); m23 = 0;
		  m30 = 0; m31 = 0; m32 = 0; m33 = 1;
		  return this;
	}
	
	//Inverse rotation by y
	public Matrix4 getRotationY(float angle) {
		  m00 = (float)Math.cos(angle); m01 = 0; m02 = (float)-Math.sin(angle); m03 = 0;
		  m10 = 0; m11 = 1; m12 = 0; m13 = 0;
		  m20 = (float)Math.sin(angle); m21 = 0; m22 = (float)Math.cos(angle); m23 = 0;
		  m30 = 0; m31 = 0; m32 = 0; m33 = 1;
		  return this;
	}
	
	//Inverse rotation by z
	public Matrix4 getRotationZ(float angle) {
		  m00 = (float) Math.cos(angle); m01 = (float) -Math.sin(angle); m02 = 0; m03 = 0;
		  m10 = (float) Math.sin(angle); m11 = (float) Math.cos(angle); m12 = 0; m13 = 0;
		  m20 = 0; m21 = 0; m22 = 1; m23 = 0;
		  m30 = 0; m31 = 0; m32 = 0; m33 = 1;
		  return this;
	}

	public float[] getValuesAsArray() {
		// TODO hier Werte in einem Float-Array mit 16 Elementen (spaltenweise gefüllt) herausgeben
		float array[] = {m00, m01, m02, m03,
				  		 m10, m11, m12, m13,
				  		 m20, m21, m22, m23,
				  		 m30, m31, m32, m33};
		return array;
	}
}




//
//
////Alle Operationen ändern das Matrixobjekt selbst und geben das eigene Matrixobjekt zurück
////Dadurch kann man Aufrufe verketten, z.B.
////Matrix4 m = new Matrix4().scale(5).translate(0,1,0).rotateX(0.5f);
//public class Matrix4 {
//    float[] values = new float[16];
//    /*	x=		0 1 2  3
//
//        y=0 	0 4 8  12
//    * 	y=1 	1 5 9  13
//    * 	y=2 	2 6 10 14
//    * 	y=3 	3 7 11 15
//    * */
//    public Matrix4() {
//        // TODO mit der Identitätsmatrix initialisieren
//        this.values[0]=1;
//        this.values[5]=1;
//        this.values[10]=1;
//        this.values[15]=1;
//    }
//
//    public Matrix4(Matrix4 toCopyFrom) {
//        // TODO neues Objekt mit den Werten von "copy" initialisieren
//        System.arraycopy(toCopyFrom.values, 0, this.values, 0, values.length);
//
//    }
//
//    public Matrix4(float near, float far) {
//        // TODO erzeugt Projektionsmatrix mit Abstand zur nahen Ebene "near" und Abstand zur fernen Ebene "far", ggf. weitere Parameter hinzufügen
//        this.values[0]=1;
//        this.values[5]=1;
//        this.values[10]= (-far-near)/(far-near);
//        this.values[11]=-1;
//        this.values[14]= (-near * 2 * far)/(far - near);
//
//    }
//
//    public Matrix4 multiply(Matrix4 other) {
//        // TODO hier Matrizenmultiplikation "this = other * this" einfügen
//        float[] tempvalues = new float[4];
//        for (int x = 0; x < 4; x++) {
//            for (int y = 0; y < 4; y++) {
//                tempvalues[y]= other.values[y] * this.values[x*4] +
//                        other.values[y+4] * this.values[x*4+1] +
//                        other.values[y+8] * this.values[x*4+2] +
//                        other.values[y+12] * this.values[x*4+3];
//            }
//            System.arraycopy(tempvalues, 0, this.values, x*4, 4);
//        }
//        return this;
//    }
//	/*		x=	0 1 2  3
//
//		y=0 	0 4 8  12
//	* 	y=1 	1 5 9  13
//	* 	y=2 	2 6 10 14
//	* 	y=3 	3 7 11 15
//	* */
//
//    public Matrix4 translate(float x, float y, float z) {
//        // TODO Verschiebung um x,y,z zu this hinzufügen
//        Matrix4 tempMat = new Matrix4();
//        tempMat.values[12]=x;
//        tempMat.values[13]=y;
//        tempMat.values[14]=z;
//        this.multiply(tempMat);
//        return this;
//    }
//
//    public Matrix4 scale(float uniformFactor) {
//        // TODO gleichmäßige Skalierung um Faktor "uniformFactor" zu this hinzufügen
//        Matrix4 tempMat = new Matrix4();
//        tempMat.values[0]=uniformFactor;
//        tempMat.values[5]=uniformFactor;
//        tempMat.values[10]=uniformFactor;
//        this.multiply(tempMat);
//        return this;
//    }
//
//    public Matrix4 scale(float sx, float sy, float sz) {
//        // TODO ungleichförmige Skalierung zu this hinzufügen
//        Matrix4 tempMat = new Matrix4();
//        tempMat.values[0]=sx;
//        tempMat.values[5]=sy;
//        tempMat.values[10]=sz;
//        this.multiply(tempMat);
//        return this;
//    }
//
//    public Matrix4 rotateX(float angle) {
//        // TODO Rotation um X-Achse zu this hinzufügen
//        Matrix4 tempMat = new Matrix4();
//        tempMat.values[5]=(float)Math.cos(angle);
//        tempMat.values[6]=(float)Math.sin(angle);
//        tempMat.values[9]=(float)Math.sin(-angle);
//        tempMat.values[10]=(float)Math.cos(angle);
//        this.multiply(tempMat);
//        return this;
//    }
//
//    public Matrix4 rotateY(float angle) {
//        // TODO Rotation um Y-Achse zu this hinzufügen
//        Matrix4 tempMat = new Matrix4();
//        tempMat.values[0]=(float)Math.cos(angle);
//        tempMat.values[2]=(float)Math.sin(angle);
//        tempMat.values[8]=(float)Math.sin(-angle);
//        tempMat.values[10]=(float)Math.cos(angle);
//        this.multiply(tempMat);
//        return this;
//    }
//
//    public Matrix4 rotateZ(float angle) {
//        // TODO Rotation um Z-Achse zu this hinzufügen
//        Matrix4 tempMat = new Matrix4();
//        tempMat.values[0]=(float)Math.cos(angle);
//        tempMat.values[1]=(float)Math.sin(angle);
//        tempMat.values[4]=(float)Math.sin(-angle);
//        tempMat.values[5]=(float)Math.cos(angle);
//        this.multiply(tempMat);
//        return this;
//    }
//
//    public float[] getValuesAsArray() {
//        // TODO hier Werte in einem Float-Array mit 16 Elementen (spaltenweise gefüllt) herausgeben
//        return this.values;
//    }
//}
//
