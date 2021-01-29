close all; clear; clc;
load('data.mat');

data(isnan(data)) = Inf;
data = -1 * data;

x = linspace(0, 160, size(data, 1));
y = linspace(0, 60, size(data, 2));
z = [0, 250, 500, 700, 950, 1200, 1430, 1600];

fig1 = figure('Position',[100 100 500 400]); 
hold on; grid minor;
set(gca, 'XMinorGrid', 'on')
set(gca, 'YMinorGrid', 'on')
view(-16.5,20);

title('Obstruction Points','Interpreter','latex')
xlabel('x (cm)','Interpreter','latex')
ylabel('y (cm)','Interpreter','latex')
zlabel('z (cm)','Interpreter','latex')

[X, Y, Z] = meshgrid(x, y, z);

for i = 1 : length(z)
    XX = X(:,:,i);
    YY = Y(:,:,i); 
    ZZ = Z(:,:,i);
    DATA = data(:,:,i)';
    id = DATA < -80;
    fig1(1) = scatter3(XX(id), YY(id), ZZ(id), 30, 'filled', 'b');
    fig1(2) = scatter3(XX(id), YY(id), zeros(size(XX(id))), 10, 'r', 'x');
end

% Draw metal supports
for i = 1 : length(z)/2
    fig1(3) = plot3([50 50],[0 max(y)],[z(i*2-1) z(i*2-1)], 'k','LineWidth',1.5);
    fig1(3) = plot3([110 110],[0 max(y)],[z(i*2-1) z(i*2-1)], 'k', 'LineWidth',1.5);
end

% Draw antenna
centera = [80,30];
ya = 55.7;
xa = 13.7;
p1 = [centera(1)-xa/2, centera(2)-ya/2 ,0];
p2 = [centera(1)-xa/2, centera(2)+ya/2 ,0];
p3 = [centera(1)+xa/2, centera(2)+ya/2 ,0];
p4 = [centera(1)+xa/2, centera(2)-ya/2 ,0];
 
Xca = [p1(1) p2(1) p3(1) p4(1)];
Yca = [p1(2) p2(2) p3(2) p4(2)];
Zca = [p1(3) p2(3) p3(3) p4(3)];
 
fig1(4) = fill3(Xca, Yca, Zca, 'm');

legend(fig1,'Obstruction Points', 'Obstruction Points z=0', 'Support metal bars', 'Antenna', 'Location','southeast', 'Interpreter','latex');
