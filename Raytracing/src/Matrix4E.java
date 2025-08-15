//Alle Operationen ändern das Matrixobjekt selbst und geben das eigene Matrixobjekt zurück
//Dadurch kann man Aufrufe verketten, z.B.
//Matrix4 m = new Matrix4().scale(5).translate(0,1,0).rotateX(0.5f);
public class Matrix4E {
	float[] values = new float[16];
	/*	x=		0 1 2  3

		y=0 	0 4 8  12
	* 	y=1 	1 5 9  13
	* 	y=2 	2 6 10 14
	* 	y=3 	3 7 11 15
	* */
	public Matrix4E() {
		// TODO mit der Identitätsmatrix initialisieren
		this.values[0]=1;
		this.values[5]=1;
		this.values[10]=1;
		this.values[15]=1;
	}

	public Matrix4E(Matrix4E toCopyFrom) {
		// TODO neues Objekt mit den Werten von "copy" initialisieren
		System.arraycopy(toCopyFrom.values, 0, this.values, 0, values.length);

	}

	public Matrix4E(float near, float far) {
		// TODO erzeugt Projektionsmatrix mit Abstand zur nahen Ebene "near" und Abstand zur fernen Ebene "far", ggf. weitere Parameter hinzufügen
		values[0]=1;
		values[5]=1;
		values[10]= (-far-near)/(far-near);
		values[11]=-1;
		values[14]= (-near * 2 * far)/(far - near);

	}

	public Matrix4E multiply(Matrix4E other) {
		// TODO hier Matrizenmultiplikation "this = other * this" einfügen
		float[] tempvalues = new float[4];
		Matrix4E tmp = new Matrix4E();
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				tempvalues[y]= other.values[y] * values[x*4] +
								other.values[y+4] * values[x*4+1] +
								other.values[y+8] * values[x*4+2] +
								other.values[y+12] * values[x*4+3];
			}
			System.arraycopy(tempvalues, 0, tmp.values, x*4, 4);
		}
		return tmp;
	}


	public Matrix4E translate(float x, float y, float z) {
		// TODO Verschiebung um x,y,z zu this hinzufügen
		Matrix4E tempMat = new Matrix4E();
		tempMat.values[12]=x;
		tempMat.values[13]=y;
		tempMat.values[14]=z;

		return this.multiply(tempMat);
	}

	public Matrix4E scale(float uniformFactor) {
		// TODO gleichmäßige Skalierung um Faktor "uniformFactor" zu this hinzufügen
		Matrix4E tempMat = new Matrix4E();
		tempMat.values[0]=uniformFactor;
		tempMat.values[5]=uniformFactor;
		tempMat.values[10]=uniformFactor;

		return this.multiply(tempMat);
	}

	public Matrix4E scale(float sx, float sy, float sz) {
		// TODO ungleichförmige Skalierung zu this hinzufügen
		Matrix4E tempMat = new Matrix4E();
		tempMat.values[0]=sx;
		tempMat.values[5]=sy;
		tempMat.values[10]=sz;

		return this.multiply(tempMat);
	}

	public Matrix4E rotateX(float angle) {
		// TODO Rotation um X-Achse zu this hinzufügen
		Matrix4E tempMat = new Matrix4E();
		tempMat.values[5]=(float)Math.cos(angle);
		tempMat.values[6]=(float)Math.sin(angle);
		tempMat.values[9]=(float)Math.sin(-angle);
		tempMat.values[10]=(float)Math.cos(angle);

		return this.multiply(tempMat);
	}
	/*	x=		0 1 2  3

		y=0 	0 4 8  12
	* 	y=1 	1 5 9  13
	* 	y=2 	2 6 10 14
	* 	y=3 	3 7 11 15
	* */

	public Matrix4E rotateY(float angle) {
		// TODO Rotation um Y-Achse zu this hinzufügen
		//angle = (float)Math.toRadians(angle);
		Matrix4E tempMat = new Matrix4E();
		tempMat.values[0]=(float)Math.cos(angle);
		tempMat.values[2]=(float)Math.sin(angle);
		tempMat.values[8]=(float)Math.sin(-angle);
		tempMat.values[10]=(float)Math.cos(angle);
		return this.multiply(tempMat);
	}

	public Matrix4E rotateZ(float angle) {
		// TODO Rotation um Z-Achse zu this hinzufügen
		Matrix4E tempMat = new Matrix4E();
		tempMat.values[0]=(float)Math.cos(angle);
		tempMat.values[1]=(float)Math.sin(angle);
		tempMat.values[4]=(float)Math.sin(-angle);
		tempMat.values[5]=(float)Math.cos(angle);
		return this.multiply(tempMat);
	}

	public float[] getValuesAsArray() {
		// TODO hier Werte in einem Float-Array mit 16 Elementen (spaltenweise gefüllt) herausgeben
		return this.values;
	}

	public void printValues(){
		System.out.println(
				"0: " + values[0]+ "1: " + values[1]+ "2: " + values[2]+  "3: " + values[3]+
						"4: " + values[4]+ "5: " + values[5]+ "6: " + values[6]+ "7: " + values[7]+
						"8: " + values[8]+ "9: " + values[9]+ "10: " + values[10]+ "11: " + values[11]+
						"12: " + values[12]+ "13: " + values[13]+ "14: " + values[14]+ "15: " + values[15] );
	}
}
