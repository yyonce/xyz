package x.y.z.AroJ.v.Color;

import android.support.v4.graphics.ColorUtils;
import android.graphics.Bitmap;
import android.graphics.Color;
import java.lang.IllegalArgumentException;
import java.lang.Math;
import java.util.Random;
import java.lang.Deprecated;



// <TEST | DEBUG>
//
//6 3 24 (10 (11 12 13) 3 24)
abstract class Palette
{ 
  @Deprecated
  public static final int TYPE_AUTO = -1;
  public static final int TYPE_DEFAULT = 0;
  public static final int TYPE_LIGHT = 1; 
  public static final int TYPE_NOLIGHT = 2;



  Palette() { 
  }




  static final class Help // <FIXED>
  {
    static final int Q_VALUE = 5;
    static final int Q_VALUE_2 = 31;
    static final int C_BLUE = -1;


    static int _To(int i, int i2, int i3) { 
      return ((1 << i3) + C_BLUE) & (i3 > i2 ? i << (i3 - i2) : i >> (i2 - i3));
    }


    static final int _To(int i) {
      final int r = _To(Color.red(i), 8, Q_VALUE);
      final int g = _To(Color.green(i), 8, Q_VALUE);
      return ((r << 10) | (g << Q_VALUE)) | _To(Color.blue(i), 8, Q_VALUE); 
    }

    //
    static final int toInt(int i) { 
      i = _To(i); 
      final int r = (i >> 10) & Q_VALUE_2;
      final int g = (i >> Q_VALUE) & Q_VALUE_2;
      final int b = i & Q_VALUE_2;
      return Color.rgb(_To(r, Q_VALUE, 8), _To(g, Q_VALUE, 8), _To(b, Q_VALUE, 8));
    } 


  }




  static final class Target
  { 
    static final float PROP_LIGHT = 1.5f; // 比
    static final float PROP_DOS = 1.45f;
    //
    static final float LIGHT_DEFAULT_L = 0.3f;
    static final float LIGHT_DEFAULT = 0.5f;
    static final float LIGHT_DEFAULT_H = 0.7f; 

    static final float LIGHT_LIGHT_L = 0.55f;
    static final float LIGHT_LIGHT = 0.74f; 
    static final float LIGHT_LIGHT_H = 1f; 

    static final float LIGHT_NOLIGHT_L = LIGHT_DEFAULT_L;
    static final float LIGHT_NOLIGHT = LIGHT_DEFAULT;
    static final float LIGHT_NOLIGHT_H = LIGHT_DEFAULT_H;
    //
    static final float DOS_DEFAULT_L = 0.35f;
    static final float DOS_DEFAULT = 1f;
    static final float DOS_DEFAULT_H = 1f; 

    static final float DOS_LIGHT_L = DOS_DEFAULT_L;
    static final float DOS_LIGHT = DOS_DEFAULT;
    static final float DOS_LIGHT_H = DOS_DEFAULT_H; 

    static final float DOS_NOLIGHT_L = 0f;
    static final float DOS_NOLIGHT = 0.3f;
    static final float DOS_NOLIGHT_H = 0.4f;


    final int TYPE;
    boolean START; // 已比分了
    float SCORE;
    int COLOR;
    int POP = 0; 
    int OLDCOLOR;


    Target(int k) {
      TYPE = k; 
    }


    static final boolean isOk(int k, float[] z) {
      final float v = z[2];
      final float vv = z[1]; 
      if (k == TYPE_DEFAULT) { 
        return (v > LIGHT_DEFAULT_L && v < LIGHT_DEFAULT_H) && (vv > DOS_DEFAULT_L && vv < DOS_DEFAULT_H);
      }
      if (k == TYPE_LIGHT) {
        return (v > LIGHT_LIGHT_L && v < LIGHT_LIGHT_H) && (vv > DOS_LIGHT_L && vv < DOS_LIGHT_H);
      }
      if (k == TYPE_NOLIGHT) {
        return (v > LIGHT_NOLIGHT_L && v < LIGHT_NOLIGHT_H) && (vv > DOS_NOLIGHT_L && vv < DOS_NOLIGHT_H);
      } 
      throw new IllegalArgumentException();
    }


    static final float toSize(int k, float[] z) { // 0 - 2
      final float v = z[2];
      final float vv = z[1]; 
      final float x = 1f;
      if (k == TYPE_DEFAULT) { 
        return (PROP_LIGHT * (x - Math.abs(v - LIGHT_DEFAULT))) + (PROP_DOS * (x - Math.abs(vv - DOS_DEFAULT))); 
      }
      if (k == TYPE_LIGHT) { 
        return (PROP_LIGHT * (x - Math.abs(v - LIGHT_LIGHT))) + (PROP_DOS * (x - Math.abs(vv - DOS_LIGHT))); 
      }
      if (k == TYPE_NOLIGHT) { 
        return (PROP_LIGHT * (x - Math.abs(v - LIGHT_NOLIGHT))) + (PROP_DOS * (x - Math.abs(vv - DOS_NOLIGHT))); 
      } 
      throw new IllegalArgumentException();
    }


    final void onCount(boolean z) {
      if (z && POP >= 0) {
        POP++; 
        final int k = 6;
        final float v = 6f;
        SCORE = SCORE + (((float) POP / (float) k) / v); 
        if (POP >= k) { 
          POP = -1; 
        }
      }
    }


    final void toScore(int i, float[] z) { // 比分 COLORS, COLOR, HSL
      final boolean zz = START && i == COLOR;
      onCount(zz);
      if (!zz && OLDCOLOR != i && isOk(TYPE, z)) { 
        POP = 0; 
        final float l = toSize(TYPE, z); // !! 
        if (!START || l > SCORE) { // OK
          SCORE = l;
          COLOR = i; // >> START
          START = true; 
        } else {
          OLDCOLOR = i; 
        }
      }
    }


  }




  static final class Filter
  {
    static final boolean isUse(float[] z) { // HSL
      final float v = z[2]; 
      final float vv = z[1];
      final float vvv = z[0];
      return v >= 0.06f && v <= 0.94f
      && !(vvv >= 10f && vvv <= 37f && vv <= 0.82f);
    }
  }




  static final class Builder
  { 
    static final int DEFAULT_SIZE;

    static {
      final int i = 112; // <FIXED>
      DEFAULT_SIZE = i * i; 
    }

    final Bitmap BITP; 


    Builder(Bitmap v) {
      if (v == null || v.isRecycled()) {
        throw new IllegalArgumentException(">> NULL ?!"); 
      } 
      BITP = v; 
    }


    final int[] init(Bitmap v) { 
      int i = v.getWidth();
      int ii = v.getHeight(); 
      final int l = i * ii; 
      if (l > DEFAULT_SIZE) {
        final double z = Math.sqrt((double) DEFAULT_SIZE / (double) l); 
        if (z > 0) {
          final int x = (int) Math.ceil(((double) i * z));
          final int y = (int) Math.ceil(((double) ii * z));
          v = Bitmap.createScaledBitmap(v, x, y, false); 
          i = x;
          ii = y;
        }
      } 
      final int[] u = new int[i * ii];
      v.getPixels(u, 0, i, 0, 0, i, ii); 
      if (v != BITP) { 
        v.recycle();
      } 
      return u; 
    }


    boolean OVER;

    final Target[] TARGET = { // <FIXED>
      new Target(TYPE_DEFAULT), 
      new Target(TYPE_LIGHT),
      new Target(TYPE_NOLIGHT)
    }; 


    final void initColor() {
      if (!OVER) { 
        final int[] l = init(BITP);
        final float[] z = new float[3]; 
        final int n = l.length;
        for (int _ = 0; _ < n; _++) { 
          final int ll = l[_]; 
          ColorUtils.colorToHSL(ll, z); 
          if (Filter.isUse(z)) { 
            for (Target v : TARGET) { 
              v.toScore(ll, z); 
            } 
          }
        } 
        OVER = true;
      }
    }


    final int initTarget(int k, int i) { 
      for (Target v : TARGET) { 
        if (v.TYPE == k) { 
          if (k == TYPE_DEFAULT) {
            for (Target vv : TARGET) {
              if (vv.START) {
                return vv.COLOR; 
              } 
            } 
            break;
          }
          if (k == TYPE_LIGHT || k == TYPE_NOLIGHT) {
            return v.START ? v.COLOR : i;
          } 
        }
      }
      return i;
    }


    final int initColor(int k, int i) { 
      initColor(); 
      final int l = initTarget(k, i); 
      return l == i ? i : Help.toInt(l); 
    }


    // <!>
    public final int toColor(int i, int k) { 
      if (k == TYPE_AUTO || k == TYPE_DEFAULT || k == TYPE_LIGHT || k == TYPE_NOLIGHT) { 
        if (k == TYPE_AUTO) { 
          final int ii = new Random().nextInt(3); // 0-2
          if (ii == 0) {
            k = TYPE_DEFAULT; 
          } else if (ii == 1) {
            k = TYPE_LIGHT; 
          } else if (ii == 2) {
            k = TYPE_NOLIGHT; 
          } 
        }
        return initColor(k, i); 
      }
      throw new IllegalArgumentException(">> NO TYPE ?!");
    }


  }



}
