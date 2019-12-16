import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;
import java.lang.Object;


public class pluginBrightness implements PlugInFilter {

public int setup(String args, ImagePlus im) {
	return DOES_ALL;
}

public void run(ImageProcessor ip) {

int M = ip.getWidth();
 int N = ip.getHeight();
int l = 256;

int[] H = ip.getHistogram();
double[] H1 = new double[]{0,1,1,39,103,206,259,335,352,466,497,
590,728,833,927,1017,1104,1097,1032,1017,1120,1135,1062,992,
1013,1022,1019,947,963,867,780,783,695,634,596,645,525,572,550,
562,482,478,472,455,436,439,442,411,390,379,406,358,361,327,356,
362,330,363,347,315,334,334,332,331,332,307,341,283,312,297,314,
319,328,319,324,318,349,328,353,338,330,326,314,361,375,375,
346,395,350,360,365,359,377,416,384,393,433,463,439,493,471,496,
523,512,571,545,590,582,620,641,629,706,719,740,739,666,667,645,
652,567,614,649,669,772,751,727,665,652,664,653,614,597,597,
581,587,580,577,594,591,602,625,544,570,587,597,578,656,699,744,
697,673,643,638,572,524,477,517,510,440,458,395,292,314,266,243,
232,168,137,84,76,65,63,53,41,28,37,31,30,30,27,42,36,61,73,99,114,
148,206,242,273,302,265,285,278,263,206,182,146,113,84,65,73,62,
66,70,96,142,151,124,162,132,134,144,113,76,37,13,1,1,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
};

double[] Blue = new double[H.length];
Blue[0] = H[0];

for (int i = 1; i < H.length; i++){
    H[i] = H[i-1] + H[i];
    Blue[i] = H[i];	
    H1[i] = H1[i -1] + H1[i];	
}

for (int i = 0; i < l; i++){
     Blue[i] = Blue[i] / Blue[l-1];
     H1[i] = H1[i] / H1[l-1]; 	
}


int[] result = matchHistograms(Blue, H1);

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