close all; clear; clc;
load('data.mat');
constdata = data;

data(isnan(data))=Inf;
data = -1 * data;

x = linspace(0, 160, size(data, 1)+1);
y = linspace(0, 60, size(data, 2)+ 1);
z = [0, 250, 500, 700, 950, 1200, 1430, 1600];

[X, Y] = meshgrid(x, y);

% Surf Plot Flat
fig2 = figure('Position',[100 100 500 400]);
title('Shelve RF Survey','Interpreter','latex')
hold on; grid minor;
set(gca, 'XMinorGrid', 'on')
set(gca, 'YMinorGrid', 'on')
axis('equal');
axis('square');
h = colorbar;
ylabel(h, 'dBm','Interpreter','latex')
view(-7,8);

xlabel('x (cm)','Interpreter','latex')
ylabel('y (cm)','Interpreter','latex')
zlabel('z (cm)','Interpreter','latex')

for i = 1 : size(data, 3)
    Z = z(i) * ones(size(X));
    surf(X,Y,Z,data(:,:,i)');
end

shading('faceted');
print(fig2,'-depsc','-r700','rfsurvey.eps');
