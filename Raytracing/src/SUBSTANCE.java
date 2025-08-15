public enum SUBSTANCE {


    air(1f),
    glass(1.5f),
    water(1.3f),
    diamond(1.8f),
    solid(0f);

    private float brechungsIndex;

    private SUBSTANCE(float brechungsIndex){
        this.brechungsIndex = brechungsIndex;
    }

    public float getBrechungsIndex() {
        return brechungsIndex;
    }
}