
Ext.require('Ext.tab.*');

Ext.onReady(function(){
    // basic tabs 1, built from existing content
    var tabs = Ext.createWidget('tabpanel', {
        layout: 'fit',
        renderTo: 'tabs1',
        activeTab: 0,
        defaults :{
            bodyPadding: 10
        },
        items: [{
            title: 'Construct Cubes',
            contentEl:'construct'
        },{
            title: 'Explore Cubes',
            contentEl:'explore'
        }]
    });
});


