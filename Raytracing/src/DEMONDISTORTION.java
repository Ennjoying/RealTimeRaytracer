public class DEMONDISTORTION {


    public AxisSelector selectedAxis;
    public Vec3f posToDistortAround;

    int turns;

    public DEMONDISTORTION(AxisSelector selectedAxis, Vec3f posToDistortAround, int turns) {
        this.selectedAxis = selectedAxis;
        this.posToDistortAround = posToDistortAround;
        this.turns = turns;
    }

    public Vec3f demonDistortion(Vec3f intersectionPoint, float maxValue){

        intersectionPoint = intersectionPoint.subtractVector(posToDistortAround);
        Matrix4E rotate = new Matrix4E();
        float factorTurns = (float)(turns*Math.PI)/maxValue;
        switch (selectedAxis){
            case x -> rotate = rotate.rotateX(factorTurns*intersectionPoint.x);
            case y -> rotate = rotate.rotateY(factorTurns*intersectionPoint.y);
            case z -> rotate = rotate.rotateZ(factorTurns*intersectionPoint.z);
        }
        return intersectionPoint.multiplyWithMatrix4(rotate).addVector(posToDistortAround);
    }

}
