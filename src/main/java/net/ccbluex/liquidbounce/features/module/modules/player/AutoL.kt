package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.Entity
import java.util.*

@ModuleInfo(name = "AutoL", category = ModuleCategory.PLAYER)
class AutoSay : Module() {
    val modeValue = ListValue("Mode", arrayOf("Chinese", "English", "Lvziqiao", "None"), "Lvziqiao")
    val QQorWebValue = ListValue("XC-Mode", arrayOf("Web","QQ"),"Web").displayable{modeValue.equals("Lvziqiao")}
    val Hytmode = BoolValue("HytMode",false)
    var R = Random()
    var abuse = arrayOf("[Fixxed-FREE] ʧ����")
    var englishabuse = arrayOf("[Fixxed-FREE] |_")
    var Lvziqiao = arrayOf(
        "�����Ա������ǣ������ѧ�ڼ�Sk1d����Fixxed С��� Gr0up "+LiquidBounce.QQ_GROUP,
        "�����Ա������ǣ�������ʹ��Fixxed Sk1d��˱��������fvv  С��� Gr0up "+LiquidBounce.QQ_GROUP,
        "���������Ǵ��ߣ������������è�����뱩���ң����ҹ��ٳ����  С��� Gr0up "+LiquidBounce.QQ_GROUP,
        "�����޵е������ǣ������Ҫħ���ҵĻ����Ҿ͹��ٿ�����ӣ����������绰  С��� Gr0up "+LiquidBounce.QQ_GROUP,
        "�����������Ǵ�����Ҽ��Ŭ����114514�꣬����Sk1d����Fixxed��Cl1ent  С��� Gr0up "+LiquidBounce.QQ_GROUP
    )
    var Lvziqiao2 = arrayOf(
        "�����Ա������ǣ������ѧ�ڼ�Sk1d����Fixxed  Web: "+LiquidBounce.CLIENT_WEBSITE2,
        "�����Ա������ǣ�������ʹ��Fixxed Sk1d��˱��������fvv Web: "+LiquidBounce.CLIENT_WEBSITE2,
        "���������Ǵ��ߣ������������è�����뱩���ң����ҹ��ٳ����  Web: "+LiquidBounce.CLIENT_WEBSITE2,
        "�����޵е������ǣ������Ҫħ���ҵĻ����Ҿ͹��ٿ�����ӣ����������绰  Web: "+LiquidBounce.CLIENT_WEBSITE2,
        "�����������Ǵ�����Ҽ��Ŭ����114514�꣬����Sk1d����Fixxed��Cl1ent  Web :"+LiquidBounce.CLIENT_WEBSITE2
    )
    private var target: Entity? = null
    var kills = 0
    @EventTarget
    fun onAttack(event: AttackEvent) {
        target = event.targetEntity
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        if (target != null) {
            if (target!!.isDead) {
                when (modeValue.get()) {
                    "Chinese" -> {
                        if(Hytmode.equals(true)){
                            mc.thePlayer.sendChatMessage(
                                "@"+"[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + abuse[R.nextInt(
                                    abuse.size
                                )]
                            )
                        }else{
                            mc.thePlayer.sendChatMessage(
                                "[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + abuse[R.nextInt(
                                    abuse.size
                                )]
                            )
                        }
                        kills += 1
                        target = null
                    }
                    "English" -> {
                        kills += 1
                        if(Hytmode.equals(true)){
                            mc.thePlayer.sendChatMessage(
                                "@"+"[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + englishabuse[R.nextInt(
                                    englishabuse.size
                                )]
                            )
                        }else{
                            mc.thePlayer.sendChatMessage(
                                "[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + englishabuse[R.nextInt(
                                    englishabuse.size
                                )]
                            )
                        }

                        target = null
                    }
                    "Lvziqiao" -> {
                        if(QQorWebValue.get()=="QQ"){
                            kills += 1
                            if(Hytmode.equals(true)){
                                mc.thePlayer.sendChatMessage(
                                    "@"+"[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + Lvziqiao[R.nextInt(
                                        Lvziqiao.size
                                    )]
                                )
                            }else{
                                mc.thePlayer.sendChatMessage(
                                    "[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + Lvziqiao[R.nextInt(
                                        Lvziqiao.size
                                    )]
                                )
                            }
                            target = null
                        }
                        else{
                            kills += 1
                            if(Hytmode.equals(true)){
                                mc.thePlayer.sendChatMessage(
                                    "@"+"[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + Lvziqiao2[R.nextInt(
                                        Lvziqiao2.size
                                    )]
                                )
                            }else{
                                mc.thePlayer.sendChatMessage(
                                    "[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + Lvziqiao2[R.nextInt(
                                        Lvziqiao2.size
                                    )]
                                )
                            }
                            target = null
                        }
                    }
                    "None" -> {
                        kills += 1
                        target = null
                    }
                }
            }
        }
    }

    override val tag: String
        get() = "Kills: $kills"
}