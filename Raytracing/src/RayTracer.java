import java.util.ArrayList;
import java.util.Arrays;

public class RayTracer {

    Main main;

    public RayTracer(Main main) {
        this.main = main;
    }

    public int[] rayTrace(Camera cam,int recursionDepth, Light lightToUse) {

        int[] rgb = new int[main.resX * main.resY];
        Arrays.fill(rgb, main.backgroundColor);

        for (int x = 0; x < main.resX; x++) {
            for (int y = 0; y < main.resY; y++) {

                Vec3f direction = cam.bottomLeft.addVector(cam.right.multiply(x * main.scaleFactorX).addVector(cam.top.multiply(y * main.scaleFactorY)));
                Ray toPixelInPlane = new Ray(cam.pos,direction);

                Intersection intersec = rayCast(toPixelInPlane);

                if(Float.isFinite(intersec.s)){
                    int pos = y * main.resX + x;


                    Vec3f farbe = recursionColor(toPixelInPlane, recursionDepth, lightToUse);
                    //Vec3f farbe = lightToUse.cookTorrance(intersec, recursionColor(toPixelInPlane, 5, lightToUse));

                    farbe = farbe.pow(0.45f).multiply(255);

                    //if all light points are hit
//                    if(shadowIntensity(intersec.vecIntersec, lightToUse) == 5) {
//                    	//rgb[pos] = lights.get(lightToUse).calcLight(closestObj, intersection);
//                        // Vec3f farbe = lightToUse.cookTorrance(intersec);
//                        //intersec.obj.material.color = reflectColor(toPixelInPlane,1, 3);
//
//                        rgb[pos] = ((int)farbe.x <<16) | ((int)farbe.y <<8) | (int)farbe.z;

                    //} else {
                        //Vec3f inverseShadowRay = lightToUse.pos.subtractWithVector(intersec.vecIntersec).multiply(-1f);
                        //if there is an object in front of the light or the pixel is not in the light cone, then shadow
                        //if(objectInFrontOfLight(intersec.vecIntersec, lightToUse.pos)) { //|| !lightToUse.isInLightCone(inverseShadowRay)) {
                        //rgb[pos] = 0x333333;
                    	int farbeX = (int)farbe.x;
                    	int farbeY = (int)farbe.y;
                    	int farbeZ = (int)farbe.z;
//                    	switch(shadowIntensity(intersec.vecIntersec, lightToUse)) {
//                    		case 0: rgb[pos] = 0x000000; break; //0%
//
//                    		case 1: farbeX = clamp((int) ((int) farbe.x - 80f));
//		                    		farbeY = clamp((int) ((int) farbe.y - 80f));
//		                    		farbeZ = clamp((int) ((int) farbe.z - 80f));
//                    				break; //20%
//
//                    		case 2: farbeX = clamp((int) ((int) farbe.x - 60f));
//		                    		farbeY = clamp((int) ((int) farbe.y - 60f));
//		                    		farbeZ = clamp((int) ((int) farbe.z - 60f));
//		                    		break; //40%
//
//                    		case 3: farbeX = clamp((int) ((int) farbe.x - 40f));
//		                    		farbeY = clamp((int) ((int) farbe.y - 40f));
//		                    		farbeZ = clamp((int) ((int) farbe.z - 40f));
//                    				break; //60%
//
//                    		case 4: farbeX = clamp((int) ((int) farbe.x - 20f));
//		                    		farbeY = clamp((int) ((int) farbe.y - 20f));
//		                    		farbeZ = clamp((int) ((int) farbe.z - 20f));
//		                    		break; //80%
//
//                    		case 5: farbeX = (int)farbe.x;
//		                    		farbeY = (int)farbe.y;
//		                    		farbeZ = (int)farbe.z;
//
//                    	}

                    	//lightHitCounter/ amount of lightPoints
                    	float lightHitCounter = shadowIntensity(intersec.vecIntersec, lightToUse);
                    	float lightPointNr = lightToUse.lightPointNr;
                    	float lightIntensityPercentage = lightHitCounter/lightPointNr;

//                    	System.out.println("lightHitCounter: " + shadowIntensity(intersec.vecIntersec, lightToUse) + " lightPointNr: " + lightToUse.lightPointNr);
                    	rgb[pos] = ((int) (farbeX * lightIntensityPercentage) <<16) | ((int) (farbeY * lightIntensityPercentage) <<8) | (int) (farbeZ * lightIntensityPercentage);
                        //rgb[pos] = (farbeX  <<16) | (farbeY  <<8) | farbeZ ;


//                    }
                }

            }
        }
        System.out.println("Done");
        return rgb;
    }
    private int clamp(int value) {
    	return (value < 0? 0 : value > 255? 255 : value);
    }

    public Vec3f recursionColor(Ray ray, int depth, Light light){
        if(depth == 0){
            return new Vec3f(0);
        }
        Intersection intersec = rayCast(ray);
        if(!Float.isFinite(intersec.s)) return new Vec3f(0);

        Vec3f color = light.cookTorrance(intersec, intersec.obj.material.color).multiply(2f);
        color.clamp(0,1);
        Vec3f reflectionColor = new Vec3f(0);
        Vec3f refractionColor = new Vec3f(0);

        if(intersec.obj.material.reflectivity > 0){
            reflectionColor = recursionColor(ray.reflect(intersec), depth-1, light);
            //schattencheck at this point to reduce color if its in a shadow
        }

        if(intersec.obj.material.transmission > 0) {
            refractionColor = recursionColor(ray.refract(intersec), depth-1, light);
        }

        color = color.multiply(intersec.obj.material.roughness).addVector(reflectionColor.multiply(intersec.obj.material.reflectivity));
        color = color.multiply(1-intersec.obj.material.transmission).addVector(refractionColor.multiply(intersec.obj.material.transmission));

        //Vec3f n = intersec.parentObj.getNormale(intersec).normalize();
        //Vec3f l = light.pos.subtractWithVector(intersec.vecIntersec).normalize();
        //Vec3f out = color.multiply(n.scalar(ray.reflect(intersec).view)).multiplyWithVector(reflectionColor);
        //out = out.addVector(color.multiply(n.scalar(l)));

        return color;
    }

    public Intersection rayCast(Ray ray){
        Intersection closestIntersec = new Intersection(null,null,Float.POSITIVE_INFINITY);

        for (int i = 0; i < main.objects.size(); i++) {
            //get a list of all intersections with the object and ray
            ArrayList<Intersection> tmp = main.objects.get(i).intersect(ray);

            //save the first intersection as closest intersection
            if( !tmp.isEmpty() && closestIntersec.s >= tmp.get(0).s && closestIntersec.s > 0){
                closestIntersec = new Intersection(tmp.get(0));
                closestIntersec.parentObj = main.objects.get(i);
            }
        }
        if(Float.isFinite(closestIntersec.s)) {
            closestIntersec.vecIntersec = ray.pos.addVector(ray.view.multiply(closestIntersec.s));

           //TODO get right softcube parent class
            if(closestIntersec.parentObj.getClass().equals(new SoftCube(null, null, 0).getClass())) {
            	Ray differentRay = ray;
            	if(ray.view.x < 0) {
            		differentRay.view.x = ray.view.x * (-1);
            	}
            	if(ray.view.y < 0) {
            		differentRay.view.y = ray.view.y * (-1);
            	}

            	closestIntersec = softCubeRayCast(differentRay, closestIntersec.parentObj);

            }
        }

        return closestIntersec;
    }

    private Intersection softCubeRayCast(Ray ray, VectorObject obj) {
    	Intersection closestIntersec = new Intersection(null,null,Float.POSITIVE_INFINITY);

    	 ArrayList<Intersection> tmp = obj.intersect(ray);

         //save the first intersection as closest intersection
         if( !tmp.isEmpty() && closestIntersec.s >= tmp.get(0).s && closestIntersec.s > 0){
             closestIntersec = new Intersection(tmp.get(0));
             closestIntersec.parentObj = obj;
         }

         if(Float.isFinite(closestIntersec.s)) {
             closestIntersec.vecIntersec = ray.pos.addVector(ray.view.multiply(closestIntersec.s));
         }

    	return closestIntersec;
    }

    // to call when there is an intersection
    //sends another ray from intersection point to the light and checks if there is an object in front

    public boolean isObjectInFrontOfLight(Vec3f intersection, Vec3f lightPos) {
    	Vec3f rayDirection = lightPos.subtractVector(intersection);

    	//We want to take a point a bit above the intersection point
    	Vec3f pointAboveIntersection = intersection.addVector(rayDirection.multiply(0.1f));


    	Ray rayFromIntersectionToLight = new Ray(pointAboveIntersection, rayDirection.normalize());

    	Intersection intersec = getFirstIntersectionBiggerThan0(rayFromIntersectionToLight);
        //Intersection intersec = rayCast(rayFromIntersectionToLight);

    	return Float.isFinite(intersec.s);

    }


    public int shadowIntensity(Vec3f intersection, Light light) {
    	int lightHitCounter = 0;
    	for (Vec3f lightPoint : light.lightPoints) {
    		//if there is no object in between, the light point gets hit
    		if(!isObjectInFrontOfLight(intersection, lightPoint)) {
    			lightHitCounter++;
    		}
    	}
    	return lightHitCounter;
    }




  //returns the first intersection > 0 of a ray with an object
    public Intersection getFirstIntersectionBiggerThan0(Ray ray){
        Intersection closestIntersec = new Intersection(null,null,Float.POSITIVE_INFINITY);
        Intersection firstIntersection = new Intersection(null,null,Float.POSITIVE_INFINITY); //placeholder to save the first intersection > 0

        for (int i = 0; i < main.objects.size(); i++) {
        	//get a list of all intersections with the object and ray
            ArrayList<Intersection> tmp = main.objects.get(i).intersect(ray);

            //get the first intersection.s that is bigger than 0
            for (Intersection intersec : tmp) {
            	if(intersec.s > 0) firstIntersection = intersec; break;
            }

            //if first intersection is not infinity -> there is an intersection
            if( !tmp.isEmpty() && closestIntersec.s >= firstIntersection.s){


            	//save the first intersection as closest intersection
                closestIntersec = new Intersection(firstIntersection);
                closestIntersec.parentObj = main.objects.get(i);
            }
        }
        if(Float.isFinite(closestIntersec.s)) {
            closestIntersec.vecIntersec = ray.pos.addVector(ray.view.multiply(closestIntersec.s));
        }

        return closestIntersec;
    }
}
/*

    // to call when there is an intersection
    //sends another ray from intersection point to the light and checks if there is an object in front
    public boolean isObjectInFrontOfLight(Vec3f intersection, Vec3f lightPos) {
        Vec3f rayDirection = lightPos.subtractVector(intersection);

        //We want to take a point a bit above the intersection point
        Vec3f pointAboveIntersection = intersection.addVector(rayDirection.multiply(0.1f));


        Ray rayFromIntersectionToLight = new Ray(pointAboveIntersection, rayDirection.normalize());

        Intersection intersec = getFirstIntersectionBiggerThan0(rayFromIntersectionToLight);
        //Intersection intersec = rayCast(rayFromIntersectionToLight);
        return Float.isFinite(intersec.s);
    }

    //TODO:
    // 1. softShadowPoints errechnen mit ebenengleichung src: https://studyflix.de/mathematik/normalenform-2404
    // 2. menge an softShadowPoints mit variable erstellen die durch update und raytrace durchgegeben wird.
    // 3. schattierungsabstufungen variabel durch lightHitCounter und den softShadowPointsAmount(maximale menge an soft shadow points).
    public int shadowIntensity(Vec3f intersection, Light light) {
        int lightHitCounter = 1;
        for (Vec3f lightPoint : light.lightPoints) {
            //if there is no object in between, the light point gets hit
            if(!isObjectInFrontOfLight(intersection, lightPoint)) {
                lightHitCounter++;
            }
        }
        return lightHitCounter;
    }


    //returns the first intersection of a ray with an object



    //returns the first intersection > 0 of a ray with an object
    public Intersection getFirstIntersectionBiggerThan0(Ray ray){
        Intersection closestIntersec = new Intersection(null,null,Float.POSITIVE_INFINITY);
        Intersection firstIntersection = new Intersection(null,null,Float.POSITIVE_INFINITY); //placeholder to save the first intersection > 0

        for (int i = 0; i < main.objects.size(); i++) {
            //get a list of all intersections with the object and ray
            ArrayList<Intersection> tmp = main.objects.get(i).intersect(ray);

            //get the first intersection.s that is bigger than 0


            //if first intersection is not infinity -> there is an intersection
            if( !tmp.isEmpty() && closestIntersec.s >= firstIntersection.s){


                //save the first intersection as closest intersection
                closestIntersec = new Intersection(firstIntersection);
                closestIntersec.parentObj = main.objects.get(i);
            }
        }
        if(Float.isFinite(closestIntersec.s)) {
            closestIntersec.vecIntersec = ray.pos.addVector(ray.view.multiply(closestIntersec.s));
        }

        return closestIntersec;
    }
}
*/
