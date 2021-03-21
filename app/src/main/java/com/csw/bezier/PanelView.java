package com.csw.bezier;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PanelView extends View {
    private static final String TAG = "PanelView";

    private Board mBoard;
    private Dot mDotStart;
    private Dot mDotControl1;
    private Dot mDotControl2;
    private Dot mDotEnd;

    private Paint mControlPaint;
    private Paint mBezierPaint;
    private Paint mTextPaint;


    public PanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBoard = new Board();
        mDotStart = new Dot(this, mBoard, true);
        mDotControl1 = new Dot(this, mBoard, false);
        mDotControl2 = new Dot(this, mBoard, false);
        mDotEnd = new Dot(this, mBoard, true);

        mControlPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mControlPaint.setColor(Color.GREEN);
        mControlPaint.setStyle(Paint.Style.STROKE);
        mControlPaint.setStrokeWidth(5f);

        mBezierPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBezierPaint.setColor(Color.BLUE);
        mBezierPaint.setStyle(Paint.Style.STROKE);
        mBezierPaint.setStrokeWidth(6f);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setStrokeWidth(2);
        mTextPaint.setTextSize(30);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.d(TAG, "onTouchEvent:" + event.getAction() + "(" + event.getX() + "," + event.getY() + ")");
        return mDotStart.touchEvent(event)
                || mDotControl1.touchEvent(event)
                || mDotControl2.touchEvent(event)
                || mDotEnd.touchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mBoard.computeWidthAndHeight(this);
        mDotStart.setCenterPoint(mBoard.getTopLeft());

        mDotControl1.setCenterPoint(mBoard.getTopCenter());

        mDotControl2.setCenterPoint(mBoard.getBottomCenter());

        mDotEnd.setCenterPoint(mBoard.getBottomRight());

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBoard.draw(canvas);
        drawBezierText(mDotStart, mDotControl1, mDotControl2, mDotEnd, canvas);
        drawLine(mDotStart, mDotControl1, canvas);
        drawLine(mDotControl1, mDotEnd, canvas);
        drawLine(mDotStart, mDotControl2, canvas);
        drawLine(mDotControl2, mDotEnd, canvas);
        drawBezier(mDotStart, mDotControl1, mDotControl2, mDotEnd, canvas);

        mDotStart.draw(canvas);
        mDotControl1.draw(canvas);
        mDotControl2.draw(canvas);
        mDotEnd.draw(canvas);

    }

    private void drawLine(Dot start, Dot end, Canvas canvas) {
        canvas.drawLine(start.getX(), start.getY(), end.getX(), end.getY(), mControlPaint);
    }

    private Path mBezierPath = new Path();

    private void drawBezier(Dot start, Dot control1, Dot control2, Dot end, Canvas canvas) {
        mBezierPath.reset();
        mBezierPath.moveTo(start.getX(), start.getY());
        mBezierPath.cubicTo(control1.getX(), control1.getY(), control2.getX(), control2.getY(), end.getX(), end.getY());
        canvas.drawPath(mBezierPath, mBezierPaint);
    }

    private void drawBezierText(Dot start, Dot control1, Dot control2, Dot end, Canvas canvas) {
        canvas.drawText("moveTo( " + start.mCenterPointVirtual.x + " , " + start.mCenterPointVirtual.y + " );"
                        + " cubicTo(" + control1.mCenterPointVirtual.x + " , " + control1.mCenterPointVirtual.y + " , "
                        + control2.mCenterPointVirtual.x + " , " + control2.mCenterPointVirtual.y + " , "
                        + end.mCenterPointVirtual.x + " , " + end.mCenterPointVirtual.y
                        + " );"
                , mBoard.getTopLeft().x / 2f, mBoard.getTopLeft().y / 2f, mTextPaint);
    }
}
