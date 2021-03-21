package com.csw.bezier;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;

public class Board {
    private static final String TAG = "Board";
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private Rect mContentRect;
    private Rect mInsetPadding;
    private Path mPath;
    private int mBoardSize;

    public Board() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        mInsetPadding = new Rect(100, 500, 100, 100);
    }

    public void computeWidthAndHeight(PanelView panelView) {
        mWidth = panelView.getMeasuredWidth();
        mHeight = panelView.getMeasuredHeight();
        mBoardSize = Math.min(mWidth, mHeight);
        int padding = mBoardSize / 3;
        mInsetPadding.set(padding, padding, padding, padding);
        int contentSize = mBoardSize - mInsetPadding.left - mInsetPadding.right;


        mContentRect = new Rect((mWidth - contentSize) / 2, (mHeight - contentSize) / 2,
                (mWidth - contentSize) / 2 + contentSize, (mHeight - contentSize) / 2 + contentSize);
        mPath = new Path();
        mPath.moveTo(mContentRect.left, mContentRect.top);
        mPath.lineTo(mContentRect.left, mContentRect.bottom);
        mPath.lineTo(mContentRect.right, mContentRect.bottom);
        mPath.lineTo(mContentRect.right, mContentRect.top);
        mPath.lineTo(mContentRect.left, mContentRect.top);
        mPath.lineTo(mContentRect.right, mContentRect.bottom);
        mPath.moveTo(mContentRect.left, mContentRect.bottom);
        mPath.lineTo(mContentRect.right, mContentRect.top);

        mPath.moveTo(mContentRect.left, mContentRect.centerY());
        mPath.lineTo(mContentRect.right, mContentRect.centerY());

        mPath.moveTo(mContentRect.centerX(), mContentRect.top);
        mPath.lineTo(mContentRect.centerX(), mContentRect.bottom);
    }

    public Rect getContentRect() {
        return mContentRect;
    }

    public Point getTopLeft() {
        return new Point(mContentRect.left, mContentRect.top);
    }

    public Point getTopCenter() {
        return new Point(mContentRect.centerX(), mContentRect.top);
    }

    public Point getBottomRight() {
        return new Point(mContentRect.right, mContentRect.bottom);
    }

    public Point getBottomCenter() {
        return new Point(mContentRect.centerX(), mContentRect.bottom);
    }

    public Point getCenter() {
        return new Point(mContentRect.centerX(), mContentRect.centerY());
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }
}
