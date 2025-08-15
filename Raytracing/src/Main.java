


import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {

     int resX = 1920/2;
     int resY = 1080/2;
     float width = 1.6f;
     float height = 0.9f;
     float scaleFactorX = width/(float)resX;
     float scaleFactorY = height/(float)resY;
     int[] pixels = new int[resX * resY];
     ArrayList<VectorObject> objects = new ArrayList<VectorObject>();
     ArrayList<Light> lights = new ArrayList<>();
     int backgroundColor = 0x333333;


     JLabel label = new JLabel();
     JFrame frame = new JFrame();

     Light light1 = new Light(new Vec3f(0, 0,20),new Vec3f(1), 1, 3, 1);

    Material red = new Material(new Vec3f(1,0,0),0.1f,0,0,SUBSTANCE.solid);
    Material beige = new Material(new Vec3f(245f/255f,245f/255f,220f/255f),.9f,0,0,SUBSTANCE.solid);
    Material green = new Material(new Vec3f(0,1,0),0.1f,0,0,SUBSTANCE.solid);
    Material blue = new Material(new Vec3f(0,0,1),0.1f,0,0,SUBSTANCE.solid);
    Material yellow = new Material(new Vec3f(1,1,0),0.1f,0,0,SUBSTANCE.solid);
    Material cyan = new Material(new Vec3f(0,1,1),1f,0,0,SUBSTANCE.solid);
    Material magenta = new Material(new Vec3f(1,0,1),0.1f,0,0,SUBSTANCE.solid);

    public static void main(String[] args) {
    	Main main = new Main();
        main.init();
        //initialize camera

    }

    public void init(){
        Vec3f camPos = new Vec3f(0,0,5); //Position der Kamera
        Vec3f view = new Vec3f(0,0,-1f); //fester Punkt in Ebene, wo Kamera hinschaut
        double angle = 0;
        Camera camera = new Camera(camPos, view, new Vec3f((float)Math.sin(angle), (float)Math.cos(angle), 0));

        //updated objs
        //main.objects.add( new CSG.Union(new Sphere(new Vec3f(-2f,-5.5f,-25f), new Vec3f(0),main.red,2f), new Sphere(new Vec3f((-main.runVar1 /10)-2f,-5f,-24f), new Vec3f(0),main.yellow,1.5f)) );
        //main.objects.add( new CSG.Difference(new Sphere(new Vec3f(-2f,-.5f,-30f), new Vec3f(0),main.green,2f), new Sphere(new Vec3f((main.runVar1 /10)-2f,0f,-29f), new Vec3f(0),main.cyan,1.5f)) );
        //main.objects.add( new CSG.Intersect(new Sphere(new Vec3f(-2f,4.5f,-20f), new Vec3f(0),main.blue,2f), new Sphere(new Vec3f((-main.runVar1 /10)-2f,5f,-19f), new Vec3f(0),main.magenta,1.5f)) );

        //main.objects.add(new Quadric(new Vec3f(main.runVar1, 0,-40), new Vec3f(0,0,0),main.magenta,1,0,1,0,0,0,0,0,0,.5f));
       /* Vec3f ApplePos = new Vec3f(0,0,-15f);
        CSG.Union Applebody1 = new CSG.Union(ApplePos,new Sphere(new Vec3f(-.8f,-.5f,0), new Vec3f(0),red,1.8f), new Sphere(new Vec3f(.9f,-.6f,.1f), new Vec3f(0),red,2.2f));
        CSG.Union Applebody2 = new CSG.Union(ApplePos,new Sphere(new Vec3f(-.8f,.5f,0), new Vec3f(0),red,1.5f), new Sphere(new Vec3f(.8f,1f,.1f), new Vec3f(0),red,1.5f));
        CSG.Union Apple1 = new CSG.Union(ApplePos,Applebody1, Applebody2);
        Sphere AppleBite = new Sphere(new Vec3f(.1f,-.2f,.7f), new Vec3f(0),beige,2f);
        CSG.Difference bittenApple1 = new CSG.Difference(ApplePos, Apple1, AppleBite);
        objects.add(bittenApple1);*/

        //not updated objs
        //main.objects.add(new Sphere(new Vec3f(5,5,-15), new Vec3f(0),main.magenta,1f));
        //main.objects.add(new Sphere(new Vec3f(-5,0,-30), new Vec3f(0),main.yellow,1f));
        //main.objects.add(new Sphere(new Vec3f(4,-1,-15), new Vec3f(0),main.cyan,1.5f));
        //glassball
        //main.objects.add(new Sphere(new Vec3f(1,0,-10), new Vec3f(0),new Material( new Vec3f(1),0.01f,0,1,SUBSTANCE.glass),1f));

        cube = new RM_Cube(new Vec3f(0,0,-25),blue, new Vec3f(1,15,1));
        cube.setDemonDistortion(AxisSelector.y,cube.pos, 2);
        objects.add(cube);


        Sphere redSphere1 = new Sphere(cube.pos.addVector(new Vec3f(2,0,0)),red,1f);
        Sphere redSphere2 = new Sphere(cube.pos.addVector(new Vec3f(-2,0,0)),red,1f);
        Sphere redSphere3 = new Sphere(cube.pos.addVector(new Vec3f(0,0,2)),red,1f);
        Sphere redSphere4 = new Sphere(cube.pos.addVector(new Vec3f(0,0,-2)),red,1f);
        objects.add( new Sphere(redSphere1));
        objects.add( new Sphere(redSphere2));
        objects.add( new Sphere(redSphere3));
        objects.add( new Sphere(redSphere4));

        SoftCube softCube = new SoftCube(new Vec3f(0,0,-10), yellow, 1f);
        //SoftCube softCube = new SoftCube(new Vec3f(0,0,-10), new Vec3f(0), yellow, 1f, new Vec3f(2));
        //softCube.setDemonDistortion(AxisSelector.y,softCube.pos);
        //objects.add(softCube);

        //setup lights
        Light light1 = new Light(new Vec3f(runVar1, 0,20),new Vec3f(1), 300f, 5, 1);
        //Light light2 = new Light(new Vec3f(-2,5,-6),new Vec3f(1), 50);
        //Light light3 = new Light(new Vec3f(0,-10,-5),new Vec3f(1), 50);

//        //setup lights, milanas lights
//        Light light1 = new Light(new Vec3f(main.runVar1, 0,-2),new Vec3f(255,255,255), 150, 4, 20);
//        Light light2 = new Light(new Vec3f(-2,5,-6),new Vec3f(255,255,255), 50, 10, 5);
//        Light light3 = new Light(new Vec3f(0,-10,-5),new Vec3f(255,255,255), 50, 2, 7);

        //main.pixels = main.rayTrace(camera,light2);
        createWindow(frame);
        RayTracer tracer = new RayTracer(this);
        while(true){
            update(tracer, camera);
        }
    }

    RM_Cube cube;
    float runVar1 = -15;
    float slideValue= runVar1/10;

    public  void update(RayTracer tracer, Camera camera){
        if (runVar1 >= 15 || runVar1 <= -15){
            slideValue*=-1;
        }
        runVar1 += slideValue;


        //main.objects.set(0, new CSG.Union(new Sphere(new Vec3f(-2f,-5.5f,-25f), new Vec3f(0),main.red,2f), new Sphere(new Vec3f((-main.runVar1 /10)-2f,-5f,-24f), new Vec3f(0),main.yellow,1.5f)) );
        //main.objects.set(1, new CSG.Difference(new Sphere(new Vec3f(-2f,0f,-30f), new Vec3f(0),main.green,2f), new Sphere(new Vec3f((main.runVar1 /10)-2f,0f,-29f), new Vec3f(0),main.cyan,1.5f)) );
        //main.objects.set(2, new CSG.Intersect(new Sphere(new Vec3f(-2f,4.5f,-20f), new Vec3f(0),main.blue,2f), new Sphere(new Vec3f((-main.runVar1 /10)-2f,5f,-19f), new Vec3f(0),main.magenta,1.5f)) );


//        Quadric tmp = new Quadric(new Vec3f(main.runVar1, 0,-50), new Vec3f(0,0,0), new Material(new Vec3f(50,100,0),.1f,0),1,0,1,0,0,0,0,0,0,1f);
        //tmp.rotateZ(main.runVar1);

        //main.objects.set(3,tmp);
        //objects.get(0).pos.z = -20f;


        Matrix4E rotate = new Matrix4E();
        rotate = rotate.rotateY(Math.signum(slideValue)*(float)(cube.dd.turns*Math.PI)/10);
        Matrix4E rotateReverse = new Matrix4E();
        rotateReverse = rotateReverse.rotateY(-1*Math.signum(slideValue)*(float)(cube.dd.turns*Math.PI)/10);


        objects.get(1).pos.y = -runVar1;
        objects.get(2).pos.y = -runVar1;
        objects.get(3).pos.y = runVar1;
        objects.get(4).pos.y = runVar1;

        objects.get(1).pos = objects.get(1).pos.subtractVector(cube.pos).multiplyWithMatrix4(rotate).addVector(cube.pos);
        objects.get(2).pos = objects.get(2).pos.subtractVector(cube.pos).multiplyWithMatrix4(rotate).addVector(cube.pos);
        objects.get(3).pos = objects.get(3).pos.subtractVector(cube.pos).multiplyWithMatrix4(rotateReverse).addVector(cube.pos);
        objects.get(4).pos = objects.get(4).pos.subtractVector(cube.pos).multiplyWithMatrix4(rotateReverse).addVector(cube.pos);


        //HINWEIS: light muss außerhalb der Update Methode erstellt werden. Sonst wird ArrayList an Lichtpunkten immer neu überschrieben

        Light light1 = new Light(new Vec3f(runVar1, 0,-2),new Vec3f(1), 1f, 5, 1);
        //Light light2 = new Light(new Vec3f(runVar1,runVar1,-6),new Vec3f(1), 1f);

        //milanas lights
        //Light light2 = new Light(new Vec3f(main.runVar1,main.runVar1,-6),new Vec3f(255,255,255), 400, 2, 3);
        //Light light3 = new Light(new Vec3f(main.xSlide,-10,-5),new Vec3f(255,255,255), 150);

        //Light light3 = new Light(new Vec3f(main.xSlide,-10,-5),new Vec3f(255,255,255), 150);


        pixels = tracer.rayTrace(camera,2, light1);


        MemoryImageSource mis = new MemoryImageSource(resX, resY, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, resX);
        Image image = Toolkit.getDefaultToolkit().createImage(mis);
        label.setIcon(new ImageIcon(image));
    }

    public void createWindow( JFrame frame){
        MemoryImageSource mis = new MemoryImageSource(resX, resY, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, resX);
        Image image = Toolkit.getDefaultToolkit().createImage(mis);
        label = new JLabel(new ImageIcon(image));
        frame.add(label);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
