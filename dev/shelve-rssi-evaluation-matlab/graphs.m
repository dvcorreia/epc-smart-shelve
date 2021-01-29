close all; clear all; clc;
load('data.mat')

tables = {'Horizontal', 'Vertical', 'Lateral'};

fig = figure('Position',[100 100 500 500]);
for i = 1 : size(data, 3)
    subplot(3,1,i);
    h = heatmap(data(:,:,i), 'Title', tables(i));
    h.XLabel = 'x(n)';
    h.YLabel = 'y(n)';
    h.ColorLimits = [min(data(:)) max(data(:))];
    colormap default;
end