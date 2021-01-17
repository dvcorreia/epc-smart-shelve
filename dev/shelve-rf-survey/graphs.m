close all; clear; clc;
load('data.mat');
constdata = data;

data(isnan(data))=Inf;
datanotinf = constdata;
datanotinf(isnan(datanotinf))=100;

data = -1 * data;
datanotinf = -1 * datanotinf;

x = linspace(0, 150, size(data, 1));
y = linspace(0, 60, size(data, 2));
z = [0, 250, 500, 700, 950, 1200, 1430, 1600];

%% Scatter 3D
[X, Y, Z] = meshgrid(x, y, z);
fig1 = figure;

sdata = constdata;
sdata(isnan(sdata))=1000;
sdata = -1 * sdata;
scatter3(X(:), Y(:), Z(:), 15, sdata(:), 'filled');

%% Surf Plot
[X, Y] = meshgrid(x, y);
fig2 = figure('Position', [10 10 1200 400]);

% Surf Plot Interpolated

subplot(1,2,1)
title('Shelve RF Survey (Interpolated)','Interpreter','latex')
hold on; grid minor;
set(gca, 'XMinorGrid', 'on')
set(gca, 'YMinorGrid', 'on')
axis('equal');
axis('square');
view(-7,8);

xlabel('x (cm)','Interpreter','latex')
ylabel('y (cm)','Interpreter','latex')
zlabel('z (cm)','Interpreter','latex')

for i = 1 : size(datanotinf, 3)
    Z = z(i) * ones(size(X));
    surf(X,Y,Z,datanotinf(:,:,i)');
end

shading('interp');

% Surf Plot Flat
subplot(1,2,2)
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
saveas(fig2,'rfsurvey.eps');

%% Surf plot circular effect

[X, Y] = meshgrid(x, y);
fig3 = figure;

title('Circular Polarization Obstruction Effect')
hold on; grid minor;

xlabel('x (cm)')
ylabel('y (cm)')
zlabel('z (cm)')

for i = 1 : size(data, 3)
    [idx, idy] = find(isinf(data(:,:,i)));
    ZZ = z(i) * ones(size(idx));
    c = i * 10 * ones(size(idx));
    scatter3(x(idx), y(idy), ZZ, 15, c, 'filled');
end

