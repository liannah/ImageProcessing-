import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;


public class pluginred implements PlugInFilter {

public int setup(String args, ImagePlus im) {
	return DOES_ALL;
}

public void run(ImageProcessor ip) {

int M = ip.getWidth();
int N = ip.getHeight();
int l = 256;

int[] H = ip.getHistogram();
double[] H1 = new double[]{0,
1,1,40,109,223,282,369,383,475,552,682,798,913,
946,995,1050,1032,994,991,1089,1076,1052,990,992,983,1008,
941,960,855,782,772,690,635,592,650,520,577,541,561,
483,472,477,454,437,443,441,411,390,382,402,359,363,
335,352,369,327,372,353,318,323,333,337,334,328,300,341,280,319,
299,315,315,327,317,322,326,351,333,349,341,331,326,319,361,
380,378,347,393,356,361,360,364,377,414,383,392,438,470,
438,479,482,497,516,517,572,546,594,585,621,647,632,711,
719,753,736,664,676,671,697,654,763,789,726,773,760,677,598,
644,657,577,547,543,542,557,577,590,576,592,598,603,629,563,586,
592,596,582,641,690,736,696,680,635,643,577,529,475,519,512,
438,455,403,291,314,281,245,249,163,141,94,123,75,113,129,135,188,
223,274,248,294,278,271,278,239,212,183,154,105,102,72,77,70,65,
77,148,135,122,107,97,115,114,109,120,108,78,24,5,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

double[] Red = new double[H.length];
Red[0] = H[0];

for (int i = 1; i < H.length; i++){
    H[i] = H[i-1] + H[i];
    Red[i] = H[i];	
    H1[i] = H1[i -1] + H[i];	
}

for (int i = 1; i < l; i++){
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