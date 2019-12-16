import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;


public class pluginSaturation implements PlugInFilter {

public int setup(String args, ImagePlus im) {
	return DOES_ALL;
}

public void run(ImageProcessor ip) {

int M = ip.getWidth();
int N = ip.getHeight();
int l = 256;

int[] H = ip.getHistogram();
double[] H1 = new double[]{2054,	0,	11,	178,	286,	235,	396,	307,	325,	373,	594,	340,	305,	724,	732,	1039,	1192,	741,	695,	390,	351,	279,	249,	345,	349,	361,	419,	232,	460,	245,	545,	599,	95,	250,	634,	143,	633,	160,	309,	537,	226,	184,	743,	228,	85,	393,	508,	385,	139,	351,	47,	847,	233,	444,	255,	550,	459,	544,	332,	184,	592,	270,	188,	1097,	43,	299,	330,	516,	388,	274,	686,	367,	466,	134,	302,	829,	303,	343,	356,	740,	340,	577,	243,	214,	86,	1336,	237,	344,	430,	401,	459,	823,	481,	553,	560,	714,	721,	821,	1227,	855,	1019,	743,	1347,	1183,	986,	1390,	1253,	1259,	1008,	1089,	981,	1015,	961,	899,	1020,	1055,	1025,	1087,	1071,	1112,	1204,	1160,	1002,	1022,	874,	834,	716,	1405,	695,	779,	797,	819,	755,	786,	658,	718,	722,	657,	625,	635,	591,	627,	506,	469,	539,	493,	486,	449,	386,	345,	410,	340,	184,	362,	278,	280,	235,	230,	208,	178,	190,	201,	134,	180,	126,	184,	120,	158,	114,	41,	162,	89,	96,	75,	53,	58,	37,	36,	42,	29,	30,	16,	17,	18,	27,	13,	15,	24,	6,	10,	8,	12,	10,	12,	5,	12,	5,	10,	5,	8,	4,	6,	6,	1,	3,	4,	0,	0,	3,	3,	2,	2,	4,	2,	0,	2,	0,	1,	0,	1,	0,	3,	0,	1,	0,	1,	1,	1,	0,	0,	0,	1,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	3,
};

double[] Red = new double[H.length];
Red[0] = H[0];

for (int i = 1; i < H.length; i++){
    H[i] = H[i-1] + H[i];
    Red[i] = H[i];	
    H1[i] = H1[i -1] + H1[i];	
}

for (int i = 0; i < l; i++){
     Red[i] = Red[i] / Red[l-1];
     H1[i] = H1[i] / H1[l-1]; 	
}


int[] result = matchHistograms(Red, H1);

for (int v = 0; v < N; v++) {
 for (int u = 0; u < M; u++) {
 int a = ip.get(u, v);
 int b = result[a];
 ip.set(u, v, b);
}
}

}

public int[] matchHistograms (double[] hA, double[] hR) {

int K = hA.length;
 double[] PA = Cdf(hA); // get CDF of histogram hA
 double[] PR = Cdf(hR); // get CDF of histogram hR
 int[] fhs = new int[K]; // mapping fhs()

 // compute mapping function fhs():
 for (int a = 0; a < K; a++) {
 int j = K - 1;
 do {
 fhs[a] = j;
 j--;
 } while (j >= 0 && hA[a] <= hR[j]);
 }
 return fhs;
 }



public double[] Cdf (double[] h) {
 // returns the cumul. distribution function for histogram h
 int K = h.length;

 int n = 0; // sum all histogram values
 for (int i = 0; i < K; i++) {
 n += h[i];
 }

 double[] P = new double[K]; // create CDF table P
 double c = h[0]; // cumulate histogram values
 P[0] = (double) c / n;
 for (int i = 1; i < K; i++) {
 c += h[i];
 P[i] = (double) c / n;
 }
 return P;
 }


}