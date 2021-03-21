package com.csw.bezier;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Dot {
    private static final String TAG = "Dot";
    private static final int DOT_RADIUS_TOUCH = 60;
    private static final int DOT_RADIUS_DRAW = 30;
    private Paint mTextPaint;
    private Paint mPaint;
    private int mRadius;
    private float mCenterSize;
    private boolean mIsFreeze;

    private View mTarget;
    private Board mBoard;

    protected Point mCenterPoint;
    protected PointF mCenterPointVirtual;
    private Rect mDotRect;

    public Dot(View target, Board board, boolean freeze) {
        this(target, board, 0, 0, freeze);
    }

    public Dot(View target, Board board, int x, int y, boolean freeze) {
        mTarget = target;
        mBoard = board;
        mIsFreeze = freeze;
        mRadius = DOT_RADIUS_DRAW;
        mCenterSize = mRadius / 3f;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRadius / 20f);


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setStrokeWidth(mRadius / 20f);
        mTextPaint.setTextSize(Math.max(mRadius / 2f, 25));


        mCenterPoint = new Point(x, y);
        mCenterPointVirtual = new PointF(0, 0);
        mDotRect = new Rect(mCenterPoint.x - DOT_RADIUS_TOUCH, mCenterPoint.y - DOT_RADIUS_TOUCH, mCenterPoint.x + DOT_RADIUS_TOUCH, mCenterPoint.y + DOT_RADIUS_TOUCH);
    }

    public void setCenterPoint(Point point) {
        this.setCenterPoint(point.x, point.y);
    }

    public void setCenterPoint(int x, int y) {
        mCenterPoint.set(x, y);
        int boardLeft = mBoard.getContentRect().left;
        int boardTop = mBoard.getContentRect().top;
        int boardWidth = mBoard.getContentRect().width();
        int boardHeight = mBoard.getContentRect().height();
        mCenterPointVirtual.set(Math.round((x - boardLeft) * 10f / boardWidth) / 10f, Math.round((y - boardTop) * 10f / boardHeight) / 10f);
        mDotRect.set(mCenterPoint.x - DOT_RADIUS_TOUCH, mCenterPoint.y - DOT_RADIUS_TOUCH, mCenterPoint.x + DOT_RADIUS_TOUCH, mCenterPoint.y + DOT_RADIUS_TOUCH);
    }

    public int getX() {
        return mCenterPoint.x;
    }

    public int getY() {
        return mCenterPoint.y;
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.drawText("(" + mCenterPointVirtual.x + "," + mCenterPointVirtual.y + ")", mCenterPoint.x - mRadius, mCenterPoint.y - DOT_RADIUS_TOUCH - 50, mTextPaint);
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mPaint);
        canvas.drawLine(mCenterPoint.x, mCenterPoint.y - mCenterSize, mCenterPoint.x, mCenterPoint.y + mCenterSize, mPaint);
        canvas.drawLine(mCenterPoint.x - mCenterSize, mCenterPoint.y, mCenterPoint.x + mCenterSize, mCenterPoint.y, mPaint);
        canvas.restore();
    }

    private int mTouchX = -1, mTouchY = -1;
    private int mLastTouchX = -1, mLastTouchY = -1;
    private boolean mIsContains = false;

    public boolean touchEvent(MotionEvent event) {
        //Log.d(TAG, "touchEvent:" + event.getAction() + "(" + event.getX() + "," + event.getY() + ")");
        if (mIsFreeze) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsContains = mDotRect.contains(Math.round(event.getX()), Math.round(event.getY()));
                //Log.d(TAG, "touchEvent mIsContains:" + mIsContains);
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                if (!mIsContains) {
                    break;
                }
                mTouchX = Math.round(event.getX());
                mTouchY = Math.round(event.getY());
                if (mLastTouchX != mTouchX || mLastTouchY != mTouchY) {
                    setCenterPoint(mTouchX, mTouchY);
                    mTarget.postInvalidateOnAnimation();
                }
                mLastTouchX = mTouchX;
                mLastTouchY = mTouchY;
                break;
            default:
                break;
        }
        return mIsContains;
    }
}
