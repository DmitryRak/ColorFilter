
public abstract class CIELab {
	public static void rgb2lab(int R, int G, int B, double[] lab) {
		//http://www.easyrgb.com/index.php?X=MATH&H=07#text7
		//http://www.easyrgb.com/index.php?X=CALC#Result
		double r, g, b, X, Y, Z, fx,fy, fz, xr, yr, zr;
		double eps = 216.d/24389.d;

		double Xr = 95.047d;  // reference white D65
		double Yr = 100.000d;
		double Zr = 108.883d;
/*
		var_R = ( R / 255 )        //R from 0 to 1
		var_G = ( G / 255 )        //G from 0 to 1
		var_B = ( B / 255 )        //B from 0 to 1

		if ( var_R > 0.04045 ) var_R = ( ( var_R + 0.055 ) / 1.055 ) ^ 2.4
		else                   var_R = var_R / 12.92
		if ( var_G > 0.04045 ) var_G = ( ( var_G + 0.055 ) / 1.055 ) ^ 2.4
		else                   var_G = var_G / 12.92
		if ( var_B > 0.04045 ) var_B = ( ( var_B + 0.055 ) / 1.055 ) ^ 2.4
		else                   var_B = var_B / 12.92

		var_R = var_R * 100
		var_G = var_G * 100
		var_B = var_B * 100

		//Observer. = 2�, Illuminant = D65
		X = var_R * 0.4124 + var_G * 0.3576 + var_B * 0.1805
		Y = var_R * 0.2126 + var_G * 0.7152 + var_B * 0.0722
		Z = var_R * 0.0193 + var_G * 0.1192 + var_B * 0.9505
*/
	    // RGB to XYZ
	    r = R/255.d; //R 0..1
	    g = G/255.d; //G 0..1
	    b = B/255.d; //B 0..1

	    // assuming sRGB (D65)
	    if (r <= 0.04045)
	        r = r/12.92d;
	    else
	        r = Math.pow((r+0.055d)/1.055d,2.4d);

	    if (g <= 0.04045)
	        g = g/12.92d;
	    else
	        g = Math.pow((g+0.055d)/1.055d,2.4d);

	    if (b <= 0.04045)
	        b = b/12.92d;
	    else
	        b = Math.pow((b+0.055d)/1.055d,2.4d);

	    r=r*100;
	    g=g*100;
	    b=b*100;
	    
	    //Observer. = 2�, Illuminant = D65
	    X =  0.4124d *r     + 0.3576d *g 		+ 0.1805d *b;
	    Y =  0.2126d *r     + 0.7152d *g 		+ 0.0722d *b;
	    Z =  0.0193d *r     + 0.1192d *g 		+ 0.9505d *b;
 /*   
	    var_X = X / ref_X          //ref_X =  95.047   Observer= 2�, Illuminant= D65
	    var_Y = Y / ref_Y          //ref_Y = 100.000
	    var_Z = Z / ref_Z          //ref_Z = 108.883

	    if ( var_X > 0.008856 ) var_X = var_X ^ ( 1/3 )
	    else                    var_X = ( 7.787 * var_X ) + ( 16 / 116 )
	    if ( var_Y > 0.008856 ) var_Y = var_Y ^ ( 1/3 )
	    else                    var_Y = ( 7.787 * var_Y ) + ( 16 / 116 )
	    if ( var_Z > 0.008856 ) var_Z = var_Z ^ ( 1/3 )
	    else                    var_Z = ( 7.787 * var_Z ) + ( 16 / 116 )

	    CIE-L* = ( 116 * var_Y ) - 16
	    CIE-a* = 500 * ( var_X - var_Y )
	    CIE-b* = 200 * ( var_Y - var_Z )
*/
	    //XYZ to Lab
	    xr = X/Xr;
	    yr = Y/Yr;
	    zr = Z/Zr;

	    if ( xr > eps )
	        fx =   Math.pow(xr, 1/3.);
	    else
	        fx =  (7.787f*xr)+(16./116.);

	    if ( yr > eps )
	        fy =   Math.pow(yr, 1/3.);
	    else
	    fy =  (7.787f*yr)+(16./116.);

	    if ( zr > eps )
	        fz =   Math.pow(zr, 1/3.);
	    else
	        fz =  (7.787f*zr)+(16./116.);

	    lab[0] = (double)( 116 * fy ) - 16;
	    lab[1] = (double)(500*(fx-fy));
	    lab[2] = (double)(200*(fy-fz));
	} 

	public static double calculateDeltaE(double[] color1, double[] color2) {
		double DeltaE = 0;
//http://colormine.org/delta-e-calculator/
/*
	CIE-L*1, CIE-a*1, CIE-b*1          //Color #1 CIE-L*ab values
	CIE-L*2, CIE-a*2, CIE-b*2          //Color #2 CIE-L*ab values
 
	Delta E* = sqrt( ( ( CIE-L*1 - CIE-L*2 ) ^ 2 )
               + ( ( CIE-a*1 - CIE-a*2 ) ^ 2 )
               + ( ( CIE-b*1 - CIE-b*2 ) ^ 2 ) )
*/
		DeltaE = Math.sqrt(Math.pow(color1[0]-color2[0],2)+Math.pow(color1[1]-color2[1],2)+Math.pow(color1[2]-color2[2],2));
		return DeltaE;
	}
}
